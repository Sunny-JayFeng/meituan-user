package jayfeng.com.meituan.loginregistry.controller;

import jayfeng.com.meituan.loginregistry.response.ResponseMessage;
import jayfeng.com.meituan.loginregistry.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 用户请求控制层
 * @author JayFeng
 * @date 2020/08/29
 */
@RestController
@RequestMapping("/meituan/user/login_registry")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 页面初始化，获取用户信息
     * @param request 拿到 cookie，获取用户信息
     * @return
     */
    public ResponseMessage findUser(HttpServletRequest request) {
        log.info("findUser 页面初始化, 获取用户信息");
        return requestSuccess(userService.findUser(request));
    }

    /**
     * 用户通过 手机号 + 验证码 登录
     * @param paramsMap 参数
     * @param response 响应
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/loginByCode")
    public ResponseMessage loginByCode(@RequestBody Map<String, String> paramsMap, HttpServletResponse response) {
        log.info("loginByCode, 用户验证码登录 paramsMap: {}", paramsMap);
        return requestSuccess(userService.loginByCode(paramsMap, response));
    }

    /**
     * 获取短信验证码
     * @param phone 手机号
     * @return 返回 ResponseMessage 对象
     */
    @GetMapping("/getIdentifyCode/{phone}")
    public ResponseMessage getIdentifyCode(@PathVariable("phone") String phone) {
        log.info("getIdentifyCode, 获取短信验证码 phone: {}", phone);
        return requestSuccess(userService.sendIdentifyCode(phone));
    }

    /**
     * 检查输入的验证码是否正确
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @GetMapping("/checkIdentifyCode")
    public ResponseMessage checkIdentifyCode(@RequestBody Map<String, String> paramsMap) {
        log.info("checkIdentifyCode, 检查验证码是否正确 paramsMap: {}", paramsMap);
        return requestSuccess(userService.checkIdentifyCode(paramsMap));
    }

    /**
     * 通过 手机号 + 密码 登录
     * @param paramsMap 请求参数
     * @param response 响应
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/loginByPassword")
    public ResponseMessage loginByPassword(@RequestBody Map<String, String> paramsMap, HttpServletResponse response) {
        log.info("loginByPassword, 用户密码登录 paramsMap: {}", paramsMap);
        return requestSuccess(userService.loginByPassword(paramsMap, response));
    }

    /**
     * 用户退出登录
     * @param request 请求
     * @param response 响应
     * @return 返回 ResponseMessage 对象
     */
    @PutMapping("/logout")
    public ResponseMessage userLogout(HttpServletRequest request, HttpServletResponse response) {
        log.info("userLogout 用户退出登录");
        return requestSuccess(userService.userLogout(request, response));
    }

    /**
     * 用户注册
     * @param registryMessage 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/registry")
    public ResponseMessage userRegistry(@RequestBody Map<String, String> registryMessage) {
        log.info("userRegistry, 用户注册 registryMessage: {}", registryMessage);
        return requestSuccess(userService.userRegistry(registryMessage));
    }

    /**
     * 注销一个用户账号
     * @param id 用户 id
     * @return 返回 ResponseMessage 对象
     */
    @PutMapping("/cancelUserAccount/{id}")
    public ResponseMessage cancelUserAccount(@PathVariable("id") Integer id) {
        log.info("cancelUserAccount, 用户注销 id: {}", id);
        return requestSuccess(userService.cancelUserAccount(id));
    }

    /**
     * 用户取消注销
     * @param id 用户 id
     * @return 返回 ResponseMessage 对象
     */
    @PutMapping("/reuseUserAccount/{id}")
    public ResponseMessage reuseUserAccount(@PathVariable("id") Integer id) {
        log.info("reuseUserAccount 用户取消注销 id: {}", id);
        return requestSuccess(userService.reuseUserAccount(id));
    }

    /**
     * 修改密码
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/changePassword")
    public ResponseMessage changePassword(@RequestBody Map<String, String> paramsMap) {
        log.info("changePassword, 用户修改密码 paramsMap: {}", paramsMap);
        return requestSuccess(userService.changePassword(paramsMap));
    }

    /**
     * 忘记密码 -- 重置密码 -- 校验账号的安全性
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/checkAccountSafe")
    public ResponseMessage checkAccountSafe(@RequestBody Map<String, String> paramsMap) {
        log.info("checkAccountSafe, 用户重置密码 paramsMap: {}", paramsMap);
        return requestSuccess(userService.checkAccountSafe(paramsMap));
    }

    /**
     * 二次验证页面获取验证码时，需要先判断所携带的 ticket 是否有效
     * @param phone 手机号
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/checkExistsTicket/{phone}")
    public ResponseMessage checkExistsTicket(@PathVariable String phone) {
        log.info("checkExistsTicket, 检查是否存在令牌 phone: {}", phone);
        return requestSuccess(userService.checkExistsTicket(phone));
    }

    /**
     * 忘记密码 -- 重置密码 -- 需要二次校验 -- 校验验证码是否正确
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/verifyCheckIdentifyCode")
    public ResponseMessage verifyCheckIdentifyCode(@RequestBody Map<String, String> paramsMap) {
        log.info("verifyCheckIdentifyCode, 检查二次校验的验证码是否正确 paramsMap: {}", paramsMap);
        return requestSuccess(userService.verifyCheckIdentifyCode(paramsMap));
    }

    /**
     * 忘记密码 -- 重置密码
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PutMapping("/retrievePassword")
    public ResponseMessage retrievePassword(@RequestBody Map<String, String> paramsMap) {
        log.info("retrievePassword, 重置密码 paramsMap: {}", paramsMap);
        return requestSuccess(userService.retrievePassword(paramsMap));
    }

    /**
     * 校验用户电话的重复性
     * @param phone 手机号
     * @return 返回 ResponseMessage 对象
     */
    @GetMapping("/checkPhoneExist/{phone}")
    public ResponseMessage checkPhoneExist(@PathVariable("phone") String phone) {
        log.info("checkPhoneExist, 检查手机号是否存在 phone: {}", phone);
        return requestSuccess(userService.checkPhoneExist(phone));
    }

    /**
     * 校验用户身份证的重复性
     * @param idCard 身份证
     * @return 返回 ResponseMessage 对象
     */
    @GetMapping("/checkIdCardExist/{idCard}")
    public ResponseMessage checkIdCardExist(@PathVariable("idCard") String idCard) {
        log.info("checkIdCardExist, 检查身份证是否已存在 idCard: {}", idCard);
        return requestSuccess(userService.checkIdCardExist(idCard));
    }

    /**
     * 校验用户名是否存在
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/checkUserNameExist")
    public ResponseMessage checkUserNameExist(@RequestBody Map<String, String> paramsMap) {
        log.info("checkUserNameExist, 检查用户名是否已存在 paramsMap: {}", paramsMap);
        return requestSuccess(userService.checkUserNameExist(paramsMap));
    }

    /**
     * 检验邮箱是否绑定某个用户
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/checkEmailExist")
    public ResponseMessage checkEmailExist(@RequestBody Map<String, String> paramsMap) {
        log.info("checkEmailExist, 检查邮箱是否已存在 paramsMap: {}", paramsMap);
        return requestSuccess(userService.checkEmailExist(paramsMap));
    }
}
