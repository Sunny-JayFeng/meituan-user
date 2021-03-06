package jayfeng.com.meituan.loginregistry.service;

import com.google.gson.Gson;
import jayfeng.com.meituan.loginregistry.bean.User;
import jayfeng.com.meituan.loginregistry.constant.CookieConstant;
import jayfeng.com.meituan.loginregistry.constant.RedisConstant;
import jayfeng.com.meituan.loginregistry.dao.UserDao;
import jayfeng.com.meituan.loginregistry.exception.RequestForbiddenException;
import jayfeng.com.meituan.loginregistry.redis.RedisService;
import jayfeng.com.meituan.loginregistry.response.ResponseData;
import jayfeng.com.meituan.loginregistry.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户业务逻辑层
 * @author JayFeng
 * @date 2020/08/29
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private CookieManagement cookieManagement;
    @Autowired
    private IdentifyCodeManagement identifyCodeManagement;
    @Autowired
    private TicketManagement ticketManagement;
    @Autowired
    private PatternMatch patternMatch;
    @Autowired
    private RedisService redisService;
    @Autowired
    private EncryptUtil encryptUtil;
    @Autowired
    private RandomUtil randomUtil;

    // 用户有效
    private static final Integer USER_VALID = 1;
    // 用户失效
    private static final Integer USER_INVALID = 0;

    private Gson gson = new Gson();

    /**
     * 获取已登录用户对象
     * @param request request
     * @return 返回对象
     */
    public ResponseData findUser(HttpServletRequest request) {
        Object userObj = cookieManagement.getLoginUser(request, CookieConstant.USER_KEY.getCookieKey(), RedisConstant.USER_UUID_MAP.getRedisMapKey());
        if (userObj == null) return ResponseData.createSuccessResponseData("findUserInfo", null);
        User user = gson.fromJson(userObj.toString(), User.class);
        log.info("findUser 获取已登录用户信息 user: {}", user);
        return ResponseData.createSuccessResponseData("findUserInfo", user);
    }

    /**
     * 用户通过 手机号 + 验证码 登录
     * @param paramsMap 请求参数
     * @param response 响应，用于设置 cookie
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData loginByCode(Map<String, String> paramsMap, HttpServletResponse response) {
        String phone = paramsMap.get("phone");
        String identifyCode = paramsMap.get("identifyCode");
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(identifyCode) || !patternMatch.isPhone(phone)) {
            log.info("loginByCode 用户验证码登录失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        if (checkIdentifyCode(phone, identifyCode)) { // 如果验证码正确
            User user = userDao.selectOneByPhone(phone);
            if (user != null) {
                log.info("loginByCode 用户验证码登录成功, user: {}", user);
                loginSuccess(response, user);
                user.setPassword(null); // 返回前端，不展示密码
                return ResponseData.createSuccessResponseData("userLoginByCodeInfo", user);
            } else {
                log.info("loginByCode 用户验证码登录失败, 当前用户不存在, phone: {}", phone);
                return ResponseData.createFailResponseData("userLoginByCodeInfo", false, "当前用户不存在", "user_is_not_exist");
            }
        } else {
            log.info("loginByCode 用户验证码登录失败, 验证码错误");
            return ResponseData.createFailResponseData("userLoginByCodeInfo", false, "验证码错误", "identify_code_error");
        }
    }

    /**
     * 用户通过 手机号 + 密码 登录
     * @param paramsMap 请求参数
     * @param response 响应，用于设置 cookie
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData loginByPassword(Map<String, String> paramsMap, HttpServletResponse response) {
        String phone = paramsMap.get("phone");
        String password = paramsMap.get("password");
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(password) || !patternMatch.isPhone(phone)) {
            log.info("loginByPassword 用户手机号密码登录失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        User user = userDao.selectOneByPhone(phone);
        if (user == null) {
            log.info("loginByPassword 用户手机号密码登录失败, 没有此用户");
            return ResponseData.createFailResponseData("userLoginByPasswordInfo", false, "当前用户未注册, 请先注册", "user_never_registry");
        }
        // 验证明文密码和加密密码是否匹配
        if (encryptUtil.matches(password, user.getPassword())) {
            log.info("loginByPassword 用户手机号密码登录成功, user: {}", user);
            loginSuccess(response, user);
            user.setPassword(null);
            return ResponseData.createSuccessResponseData("userLoginByPasswordInfo", user);
        } else {
            log.info("loginByPassword, 用户手机号密码登录失败,账号或密码错误 phone: {}, password: {}", phone, password);
            return ResponseData.createFailResponseData("userLoginByPasswordInfo", false, "账号或密码错误，请重新输入", "phone_or_password_error");
        }
    }

    /**
     * 登录成功，设置 cookie 和 redis 缓存
     * @param response 用于设置 cookie
     */
    private void loginSuccess(HttpServletResponse response, User user) {
        String value = UUID.randomUUID().toString();
        cookieManagement.setCookie(response, CookieConstant.USER_KEY.getCookieKey(), value);
        redisService.addUUID(RedisConstant.USER_UUID_MAP.getRedisMapKey(), value, gson.toJson(user));
    }

    /**
     * 根据用户 id 设置用户是否有效
     * @param userId 用户 id
     * @param isValid 是否有效
     */
    private void setUserIsValid(Integer userId, Integer isValid) {
        log.info("setUserIsValid 设置用户是否有效 userId: {}, isValid: {}", userId, isValid);
        userDao.updateUserIsValid(userId, isValid);
    }

    /**
     * 用户退出登录
     * @param request 请求，用于获取 cookie
     * @param response 响应，用于删除 cookie
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData userLogout(HttpServletRequest request, HttpServletResponse response) {
        String userUUID = cookieManagement.removeCookie(response, request.getCookies(), CookieConstant.USER_KEY.getCookieKey());
        log.info("userLogout 用户登出成功, userUUID: {}", userUUID);
        redisService.deleteUUID(RedisConstant.USER_UUID_MAP.getRedisMapKey(), userUUID);
        return ResponseData.createSuccessResponseData("userLogout", userUUID);
    }

    /**
     * 发送短信验证码
     * @param phone 手机号
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData sendIdentifyCode(String phone) {
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone)) {
            log.info("sendIdentifyCode 验证码发送失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }

        // 如果手机号不为空并且格式正确
        String identifyCode = identifyCodeManagement.getIdentifyCode(phone);
        log.info("sendIdentifyCode, 成功获取到验证码: {}", identifyCode);
        String resultData = identifyCodeManagement.sendIdentifyCode(phone, identifyCode);
        // 处理发送结果
        Gson gson = new Gson();
        HashMap sendResult = gson.fromJson(resultData, HashMap.class);
        String message = sendResult.get("Message").toString();
        String code = sendResult.get("Code").toString();
        // 处理响应回前端的数据
        if ("OK".equals(message) && "OK".equals(code)) {
            log.info("sendIdentifyCode 验证码发送成功, identifyCode: {}", identifyCode);
            return ResponseData.createSuccessResponseData("sendIdentifyCodeInfo", true);
        } else if (message.contains("分钟级")) { // 分钟级流控 1 分钟 1条
            log.info("sendIdentifyCode 触发分钟级流控, 验证码发送失败");
            identifyCodeManagement.removeIdentifyCode(phone);
            return ResponseData.createFailResponseData("sendIdentifyCodeInfo", false, "频繁获取验证码，请1分钟后重试", "get_identify_code_busy_minute");
        } else if (message.contains("小时级")) { // 小时级流控 1 小时 5 条
            log.info("sendIdentifyCode 触发小时级流控, 验证码发送失败");
            identifyCodeManagement.removeIdentifyCode(phone);
            return ResponseData.createFailResponseData("sendIdentifyCodeInfo", false, "频繁获取验证码，请1小时后重试", "get_identify_code_busy_hour");
        } else if (message.contains("天级")) { // 天级流控 1 天 10 条
            log.info("sendIdentifyCode 触发天级流控, 验证码发送失败");
            identifyCodeManagement.removeIdentifyCode(phone);
            return ResponseData.createFailResponseData("sendIdentifyCodeInfo", false, "频繁获取验证码，请24小时后重试", "get_identify_code_busy_day");
        } else {
            log.info("sendIdentifyCode 服务器繁忙, 验证码发送失败");
            identifyCodeManagement.removeIdentifyCode(phone);
            return ResponseData.createFailResponseData("sendIdentifyCodeInfo", false, "服务器繁忙", "busy");
        }
    }

    /**
     * 检查输入的验证码是否正确
     * @param paramsMap 请求参数
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData checkIdentifyCode(Map<String, String> paramsMap) {
        String phone = paramsMap.get("phone");
        String identifyCode = paramsMap.get("identifyCode");
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(identifyCode) || !patternMatch.isPhone(phone)) {
            log.info("checkIdentifyCode 验证码校验失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        if (checkIdentifyCode(phone, identifyCode)) {
            log.info("checkIdentifyCode 验证码正确");
            return ResponseData.createSuccessResponseData("checkIdentifyCodeInfo", true);
        } else {
            log.info("checkIdentifyCode 验证码错误");
            return ResponseData.createFailResponseData("checkIdentifyCodeInfo", false, "验证码错误", "identify_code_error");
        }
    }

    /**
     * 检查输入的验证码是否正确
     * @param phone 手机号
     * @param identifyCode 验证码
     * @return 返回验证码是否正确
     */
    private Boolean checkIdentifyCode(String phone, String identifyCode) {
        String realIdentifyCode = identifyCodeManagement.getIdentifyCode(phone);
        if (identifyCode.equals(realIdentifyCode)) {
            log.info("checkIdentifyCode 验证码正确");
            identifyCodeManagement.removeIdentifyCode(phone); // 删除验证码
            return true;
        } else {
            log.info("checkIdentifyCode 验证码错误, identifyCode: {}, realIdentifyCode: {}", identifyCode, realIdentifyCode);
            return false;
        }
    }

    /**
     * 用户注册
     * @param registryMessage 请求参数
     * @return 响应数据，返回 ResponseData 对象
     */
    @Transactional
    public ResponseData userRegistry(Map<String, String> registryMessage) {
        String phone = registryMessage.get("phone");
        String password = registryMessage.get("password");
        String identifyCode = registryMessage.get("identifyCode");
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(password) || ObjectUtils.isEmpty(identifyCode) || !checkPhoneAndPassword(phone, password)) {
            log.info("userRegistry 用户注册失败失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }

        if (userDao.selectOneByPhone(phone) != null) {
            return ResponseData.createFailResponseData("userRegistryInfo", false, "该手机号已被注册, 请直接登录或找回密码", "phone_already_registry");

        } else if (checkIdentifyCode(phone, identifyCode)) {
            User user = createUser(phone, password);
            userDao.insertUser(user); // 新增一个用户
            createUserName(user); // 根据新增的用户的 id，创建一个昵称
            userDao.updateUserNameById(user); // 更新用户的昵称
            log.info("userRegistry 用户注册成功, user: {}", user);
            return ResponseData.createSuccessResponseData("userRegistryInfo", user);

        } else {
            log.info("userRegistry 用户注册失败, 用户注册失败，手机验证码不正确");
            return ResponseData.createFailResponseData("userRegistryInfo", false, "验证码错误", "identify_code_error");
        }
    }

    /**
     * 构建一个 user 对象
     * @param phone 手机号
     * @param password 密码
     * @return 返回一个 user 对象
     */
    private User createUser(String phone, String password) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(encryptUtil.encrypt(password));
        user.setUserName("占位昵称");
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }

    /**
     * 随机生成昵称
     * @param user 用户对象
     * @return 返回用户对象
     */
    private User createUserName(User user) {
        String userId = user.getId().toString(); // 根据 userId 去生成，就能保证唯一
        // 昵称 12 位
        int length = 12 - userId.length(); // 需要新生成几位
        StringBuilder str = new StringBuilder();
        String randomStr = randomUtil.getRandomString(length - 1); // 少生成一位
        str.append(randomStr);
        str.append(randomUtil.getRandomLetter()); // 生成最后一位，最后一位只能是字母或下划线
        str.append(userId);
        user.setUserName(str.toString());
        return user;
    }

    /**
     * 检验手机号跟密码是否符合要求是否正确
     * @param phone 手机号
     * @param password 密码
     * @return 返回手机号跟密码是否符合要求
     */
    private Boolean checkPhoneAndPassword(String phone, String password) {
        return (patternMatch.isPhone(phone)) && (patternMatch.checkPassword(password));
    }

    /**
     * 注销一个用户账号
     * @param id 用户 id
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData cancelUserAccount(Integer id) {
        if (ObjectUtils.isEmpty(id)) {
            log.info("cancelUserAccount 用户注销失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        User user = userDao.selectOneById(id);
        if (user == null) {
            log.info("cancelUserAccount 用户注销失败, 不存在此用户");
            return ResponseData.createFailResponseData("cancelUserAccountInfo", false, "注销失败", "cancel_error");
        }
        log.info("closeUserAccount, 注销一个用户, id: {}", id);
        setUserIsValid(id, USER_INVALID);
        log.info("closeUserAccount, 注销成功");
        return ResponseData.createSuccessResponseData("closeUserAccountInfo", true);
    }

    /**
     * 取消注销一个账号
     * @param id 用户 id
     * @return
     */
    public ResponseData reuseUserAccount(Integer id) {
        if (ObjectUtils.isEmpty(id)) {
            log.info("reuseUserAccount 用户取消注销失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        User user = userDao.selectOneById(id);
        if (user == null) {
            log.info("reuseUserAccount 用户取消注销失败, 不存在此用户");
            return ResponseData.createFailResponseData("reuseUserAccountInfo", false, "取消注销失败", "reuse_error");
        }
        log.info("reuseUserAccount, 用户取消注销, id: {}", id);
        setUserIsValid(id, USER_VALID);
        log.info("reuseUserAccount, 取消注销成功");
        return ResponseData.createSuccessResponseData("reuseUserAccountInfo", true);
    }

    /**
     * 修改密码
     * @param paramsMap 请求参数
     * @return 响应数据，返回 ResponseData 对象
     */
    @Transactional
    public ResponseData changePassword(Map<String, String> paramsMap) {
        String phone = paramsMap.get("phone");
        String oldPassword = paramsMap.get("oldPassword");
        String newPassword = paramsMap.get("newPassword");
        String identifyCode = paramsMap.get("identifyCode");
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(oldPassword) || ObjectUtils.isEmpty(newPassword) || ObjectUtils.isEmpty(identifyCode) || !patternMatch.isPhone(phone)) {
            log.info("changePassword 修改密码失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }

        User user = userDao.selectOneByPhone(phone);
        if (user != null) {
            if (checkIdentifyCode(phone, identifyCode)) {
                userDao.updateUserPasswordByPhone(phone, newPassword, System.currentTimeMillis());
                log.info("changePassword 修改密码成功, oldPassword: {}, newPassword: {}", oldPassword, newPassword);
                return ResponseData.createSuccessResponseData("changePasswordInfo", user);
            } else {
                log.info("changePassword 修改密码失败, 验证码错误");
                return ResponseData.createFailResponseData("changePasswordInfo", false, "验证码错误", "identify_code_error");
            }
        } else {
            log.info("changePassword 修改密码失败, 手机号或旧密码错误, phone: {}, oldPassword: {}", phone, oldPassword);
            return ResponseData.createFailResponseData("changePasswordInfo", false, "手机号或旧密码错误", "phone_or_old_password_error");
        }
    }

    /**
     * 忘记密码 -- 重置密码 -- 校验账号的安全性
     * @param paramsMap 请求参数
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData checkAccountSafe(Map<String, String> paramsMap) {
        String account = paramsMap.get("account");
        if (ObjectUtils.isEmpty(account)) {
            log.info("checkAccountSafe 检测安全性失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Boolean checkResult;
        User tempUser = new User();
        if (patternMatch.isPhone(account)) {
            checkResult = phoneAccountSafeCheck(tempUser, account);
        } else if (patternMatch.isEmail(account)) {
            checkResult = emailAccountSafeCheck(tempUser, account);
        } else {
            checkResult = userNameAccountSafeCheck(tempUser, account);
        }
        log.info("checkAccountSafe 账号安全性校验结果：account: {}, checkResult: {}", account, checkResult);
        if (!checkResult) {
            log.info("checkAccountSafe 账号安全性校验结果: account: {}, checkResult: 当前账号行为异常，已被限制登录或注册", account);
            return ResponseData.createFailResponseData("checkAccountSafeInfo", false, "当前账号行为异常，已被限制登录或注册", "account_is_unsafe");
        }
        Integer times = redisService.getCheckSafeTimes(tempUser.getId().toString());
        if (times > 8) {
            log.info("checkAccountSafe 重置密码操作超过限制次数 times: {}", times);
            return ResponseData.createFailResponseData("checkAccountSafeInfo", false, "操作过于频繁, 请 24 小时后重试", "frequent_operations_error");
        } else {
            log.info("checkAccountSafe 重置密码操作次数增加 1, times: {}", ++ times);
            redisService.setCheckSafeTimes(tempUser.getId().toString(), times);
        }
        // 判断是否需要进行二次校验
        Boolean needCheckAgain = needCheckAgain(tempUser.getUpdateTime());
        log.info("checkAccountSafe 是否需要进行二次校验, needCheckAgain: {}", needCheckAgain);
        if (needCheckAgain) { // 需要进行二次验证
            ticketManagement.createTicket(tempUser.getPhone()); // 生成一个 ticket
            // 需要二次验证，返回手机号码，短信验证
            log.info("checkAccountSafe 需要二次校验");
            return ResponseData.createFailResponseData("checkAccountSafeInfo", tempUser.getPhone(), "您需要进行二次校验", "need_check_again");
        } else { // 不需要进行二次验证
            log.info("checkAccountSafe 不需要二次校验");
            return ResponseData.createSuccessResponseData("checkAccountSafeInfo", true);
        }
    }

    /**
     * 二次验证页面获取验证码时，需要先判断所携带的 ticket 是否有效
     * @param phone 手机号
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData checkExistsTicket(String phone) {
        log.info("checkExistsTicket, phone: {}", phone);
        if (ObjectUtils.isEmpty(phone)) {
            log.info("checkExistsTicket 校验失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        if (ticketManagement.ticketExists(phone)) {
            log.info("checkExistsTicket 校验成功, ticket 存在");
            return ResponseData.createSuccessResponseData("checkTicketExistsInfo", true);
        } else {
            log.info("checkExistsTicket 校验失败, ticket 已过期");
            return ResponseData.createFailResponseData("checkTicketExistsInfo", false, "身份验证码已过期，请重新校验，即将返回找回登录密码页面", "over_time");
        }
    }

    /**
     * 忘记密码 -- 通过手机号重置密码 -- 校验安全性
     * @param phone 手机号
     * @return 返回账号是否安全
     */
    private Boolean phoneAccountSafeCheck(User user, String phone) {
        User tempUser = userDao.selectOneUpdateTimeByPhone(phone);
        if (tempUser == null) {
            log.info("phoneAccountSafeCheck 通过手机号重置密码，安全性校验, phone: {}, safeCheckResult: {}", phone, false);
            return false;
        } else {
            user.setId(tempUser.getId());
            user.setPhone(tempUser.getPhone());
            user.setUpdateTime(tempUser.getUpdateTime());
            log.info("phoneAccountSafeCheck 通过手机号重置密码，安全性校验, phone: {}, safeCheckResult: {}", phone, true);
            return true;
        }
    }

    /**
     * 忘记密码 -- 通过邮箱重置密码 -- 校验安全性
     * @param userName 用户名
     * @return 返回账号是否安全
     */
    private Boolean userNameAccountSafeCheck(User user, String userName) {
        User tempUser = userDao.selectOneUpdateTimeByUserName(userName);
        if (tempUser == null) {
            log.info("userNameAccountSafeCheck 通过用户名重置密码，安全性校验, userName: {}, safeCheckResult: {}", userName, false);
            return false;
        } else {
            user.setId(tempUser.getId());
            user.setPhone(tempUser.getPhone());
            user.setUpdateTime(tempUser.getUpdateTime());
            log.info("userNameAccountSafeCheck 通过用户名重置密码，安全性校验, userName: {}, safeCheckResult: {}", userName, true);
            return true;
        }
    }

    /**
     * 忘记密码 -- 通过用户名重置密码 -- 校验安全性
     * @param email 邮箱
     * @return 返回账号是否安全
     */
    private Boolean emailAccountSafeCheck(User user, String email) {
        User tempUser = userDao.selectOneUpdateTimeByEmail(email);
        if (tempUser == null) {
            log.info("emailAccountSafeCheck 通过邮箱重置密码，安全性校验, email: {}, safeCheckResult: {}", email, false);
            return false;
        } else {
            user.setId(tempUser.getId());
            user.setPhone(tempUser.getPhone());
            user.setUpdateTime(tempUser.getUpdateTime());
            log.info("emailAccountSafeCheck 通过邮箱重置密码，安全性校验, email: {}, safeCheckResult: {}", email, true);
            return true;
        }
    }

    /**
     * 是否需要二次校验 -- updateTime 距离当前没超过 14 天的，均需要二次验证
     * @param updateTime 更新时间
     * @return 返回是否需要二次校验
     */
    private Boolean needCheckAgain(Long updateTime) {
//        Boolean result = dateUtil.timeIsOverBoundTime(updateTime);
        Boolean result = true; // 目前先认定均需要二次验证
        log.info("needCheckAgain, result: {}", result);
        return result;
    }

    /**
     * 忘记密码 -- 重置密码 -- 需要二次校验
     * @param paramsMap 请求参数
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData verifyCheckIdentifyCode(Map<String, String> paramsMap) {
        String phone = paramsMap.get("phone");
        String identifyCode = paramsMap.get("identifyCode");
        if (ObjectUtils.isEmpty(phone) || ObjectUtils.isEmpty(identifyCode) || !patternMatch.isPhone(phone)) {
            log.info("verifyCheckIdentifyCode 二次校验失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }

        log.info("verifyCheckIdentifyCode 二次校验, phone: {}, identifyCode: {}", phone, identifyCode);
        User user = userDao.selectOneByPhone(phone);
        if (user != null) { // 如果当前用户存在
            if (checkIdentifyCode(phone, identifyCode)) { // 验证码正确
                log.info("verifyCheckIdentifyCode 二次校验, 验证码正确");
                String ticket = ticketManagement.getTicket(phone); // 获取校验前已生成的 ticket
                Map<String, String> infoMap = new HashMap<>(4);
                infoMap.put("user", phone);
                infoMap.put("ticket", ticket);
                return ResponseData.createSuccessResponseData("verifyCheckIdentifyCodeInfo", infoMap);
            } else { // 验证码不正确
                log.info("verifyCheckIdentifyCode 二次校验, 验证码不正确");
                return ResponseData.createFailResponseData("verifyCheckIdentifyCodeInfo", false, "验证码错误", "identify_code_error");
            }
        } else { // 如果当前用户不存在
            log.info("verifyCheckIdentifyCode 二次校验, 不存在此用户, phone: {}", phone);
            return ResponseData.createFailResponseData("verifyCheckIdentifyCodeInfo", false, "当前手机号未注册", "phone_not_have_registry");
        }

    }

    /**
     * 忘记密码 -- 重置密码
     * @param paramsMap 请求参数
     * @return 响应数据，返回 ResponseData 对象
     */
    @Transactional
    public ResponseData retrievePassword(Map<String, String> paramsMap) {
        // 到第三步的时候，无论第一步是通过邮箱还是用户名，都会转化为通过账号（手机号）
        String account = paramsMap.get("account");
        String newPassword = paramsMap.get("newPassword");
        String ticket = paramsMap.get("ticket");

        if (ObjectUtils.isEmpty(account) || ObjectUtils.isEmpty(newPassword) || ObjectUtils.isEmpty(ticket) || !checkPhoneAndPassword(account, newPassword)) {
            log.info("retrievePassword 密码重置失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }

        String realTicket = ticketManagement.getTicket(account);
        if (ObjectUtils.isEmpty(realTicket) || !ticket.equals(realTicket)) {
            log.info("retrievePassword 密码重置失败, 安全通行码已过期, ticket: {}, realTicket: {}", ticket, realTicket);
            return ResponseData.createFailResponseData("retrievePasswordInfo", false, "安全通行码已过期", "safe_code_already_time_out");
        }

        // 走到这里，证明是通过验证了，重置密码
        newPassword = encryptUtil.encrypt(newPassword);
        userDao.updateUserPasswordByPhone(account, newPassword, System.currentTimeMillis());
        log.info("retrievePassword 密码重置成功, account: {}, newPassword: {}", account, newPassword);
        ticketManagement.removeTicket(account); // 移除 ticket

        return ResponseData.createSuccessResponseData("retrievePasswordInfo", true);
    }

    /**
     * 校验用户号码的重复性
     * @param phone 手机号
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData checkPhoneExist(String phone) {
        if (ObjectUtils.isEmpty(phone) || !patternMatch.isPhone(phone)) {
            log.info("checkPhoneExist 校验用户号码的重复性失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Boolean isExist = userDao.selectOneByPhone(phone) != null;
        log.info("checkPhoneExist 校验成功, result: {}", isExist);
        return ResponseData.createSuccessResponseData("checkPhoneExist", isExist);
    }

    /**
     * 校验用户身份证的重复性
     * @param idCard 身份证
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData checkIdCardExist(String idCard) {
        if (ObjectUtils.isEmpty(idCard)) {
            log.info("checkIdCardExist 校验用户身份证的重复性失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Boolean isExist = userDao.selectOneByIdCard(idCard) != null;
        log.info("checkIdCardExist, result: {}", isExist);
        return ResponseData.createSuccessResponseData("checkIdCardExist", isExist);
    }

    /**
     * 校验用户名是否存在
     * @param paramsMap 请求参数
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData checkUserNameExist(Map<String, String> paramsMap) {
        String userName = paramsMap.get("userName");
        if (ObjectUtils.isEmpty(userName)) {
            log.info("checkUserNameExist 校验用户名是否存在失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Boolean isExist = userDao.selectOneByUserName(userName) != null;
        log.info("checkUserNameExist, result: {}", isExist);
        return ResponseData.createSuccessResponseData("checkUserNameExistInfo", isExist);
    }

    /**
     * 检验邮箱是否绑定某个用户
     * @param paramsMap 请求参数
     * @return 响应数据，返回 ResponseData 对象
     */
    public ResponseData checkEmailExist(Map<String, String> paramsMap) {
        String email = paramsMap.get("email");
        // 如果邮箱格式不正确
        if (!patternMatch.isEmail(email)) {
            log.info("checkEmailExist 检验邮箱是否绑定某个用户失败, 参数异常, 请求不是来自浏览器");
            throw new RequestForbiddenException("您无权访问该服务");
        }
        Boolean isExist = userDao.selectOneByEmail(email) != null;
        log.info("checkEmailExist, result: {}", isExist);
        return ResponseData.createSuccessResponseData("checkEmailExistInfo", isExist);
    }

    public List<User> testWriteUserDao() {
        return userDao.selectAll();
    }

    public List<User> testReadUserDao() {
        return userDao.selectAll();
    }

}
