package jayfeng.com.meituan.personalmessage.service;

import jayfeng.com.meituan.personalmessage.bean.User;
import jayfeng.com.meituan.personalmessage.dao.UserDao;
import jayfeng.com.meituan.personalmessage.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * @author JayFeng
 * @date 2021/2/12
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 修改用户头像
     * @param userId 用户 id
     * @param headImage 用户头像
     * @return 返回修改是否成功
     */
    public ResponseData changeHeadImage(Integer userId, String headImage) {
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(headImage)) {
            log.info("changeHeadImage 头像修改失败, 存在参数值为空的参数 userId: {}, headImage: {}", userId, headImage);
            return ResponseData.createFailResponseData("changeHeadImageInfo", false, "参数错误", "param_error");
        }

        if (ObjectUtils.isEmpty(userDao.selectIdById(userId))) {
            log.info("changeHeadImage 头像修改失败, 不存在此用户");
            return ResponseData.createFailResponseData("changeHeadImageInfo", false, "不存在此用户", "user_not_exists");
        }

        userDao.updateHeadImage(userId, headImage, System.currentTimeMillis());
        log.info("changeHeadImage 头像修改成功 userId: {}, headImage: {}", userId, headImage);
        return ResponseData.createSuccessResponseData("changeHeadImageInfo", true);
    }

    /**
     * 修改用户名
     * @param userId 用户id
     * @param paramsMap 参数 -- 旧昵称 新昵称
     * @return 返回修改是否成功
     */
    public ResponseData changeUserName(Integer userId, Map<String, String> paramsMap) {
        String oldUserName = paramsMap.get("oldUserName");
        String newUserName = paramsMap.get("newUserName");
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(oldUserName) || ObjectUtils.isEmpty(newUserName)) {
            log.info("changeUserName 用户名修改失败, 存在参数值为空的参数 userId: {}, oldUserName: {}, newUserName: {}", userId, oldUserName, newUserName);
            return ResponseData.createFailResponseData("changeUserNameInfo", false, "参数错误", "param_error");
        }
        if (ObjectUtils.isEmpty(userDao.selectOneByIdAndUserName(userId, oldUserName))) {
            log.info("changeUserName 用户名修改失败, 不存在此用户");
            return ResponseData.createFailResponseData("changeUserNameInfo", false, "不存在此用户", "user_not_exists");
        }

        userDao.updateUserName(userId, newUserName, System.currentTimeMillis());
        log.info("changeUserName 用户名修改成功, userId: {}, userName: {}", userId, newUserName);
        return ResponseData.createSuccessResponseData("changeUserNameInfo", true);
    }

    /**
     * 修改密码
     * @param userId 用户id
     * @param paramsMap 参数 -- 旧密码 新密码 验证码
     * @return 返回修改是否成功
     */
    public ResponseData changePassword(Integer userId, Map<String, String> paramsMap) {
        String oldPassword = paramsMap.get("oldPassword");
        String newPassword = paramsMap.get("newPassword");
        String identifyCode = paramsMap.get("identifyCode");

        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(oldPassword) || ObjectUtils.isEmpty(newPassword) || ObjectUtils.isEmpty(identifyCode)) {
            log.info("changePassword 密码修改失败, 存在参数值为空的参数 userId: {}, oldPassword: {}, newPassword: {}, identifyCode: {}", userId, oldPassword, newPassword, identifyCode);
            return ResponseData.createFailResponseData("changePasswordInfo", false, "参数错误", "param_error");
        }

        if (ObjectUtils.isEmpty(userDao.selectOneByIdAndPassword(userId, oldPassword))) {
            log.info("changePassword 密码修改失败, 不存在此用户");
            return ResponseData.createFailResponseData("changePasswordInfo", false, "不存在此用户", "user_not_exists");
        }

        String realIdentifyCode = "";
        if (!identifyCode.equals(realIdentifyCode)) {
            log.info("changePassword 密码修改失败, 验证码错误, identifyCode: {}, realIdentifyCode: {}", identifyCode, realIdentifyCode);
            return ResponseData.createFailResponseData("changePasswordInfo", false, "验证码错误", "identify_code_error");
        }

        userDao.updatePassword(userId, newPassword, System.currentTimeMillis());
        log.info("changePassword 密码修改成功, userId: {}, newPassword: {}", userId, newPassword);
        return ResponseData.createSuccessResponseData("changePasswordInfo", true);
    }

    /**
     * 修改生日
     * @param userId 用户id
     * @param birthday 生日
     * @return 返回修改是否成功
     */
    public ResponseData changeBirthday(Integer userId, Long birthday) {
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(birthday)) {
            log.info("changeBirthday 生日修改失败, 存在参数值为空的参数 userId: {}, birthday: {}", userId, birthday);
            return ResponseData.createFailResponseData("changeBirthdayInfo", false, "参数错误", "param_error");
        }
        if (ObjectUtils.isEmpty(userDao.selectIdById(userId))) {
            log.info("changeBirthday 生日修改失败, 不存在此用户");
            return ResponseData.createFailResponseData("changeBirthdayInfo", false, "不存在此用户", "user_not_exists");
        }
        userDao.updateBirthday(userId, birthday, System.currentTimeMillis());
        log.info("changeBirthday 生日修改成功");
        return ResponseData.createSuccessResponseData("changeBirthdayInfo",  true);
    }

    /**
     * 开通会员
     * @param userId 用户 id
     * @param paramsMap 参数 -- 会员类型、有效时间
     * @return 返回开通是否成功
     */
    public ResponseData openVIP(Integer userId, Map<String, String> paramsMap) {
        String type = paramsMap.get("type"); // 会员类型
        String usefulTime = paramsMap.get("usefulTime"); // 有效时间
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(type) || ObjectUtils.isEmpty(usefulTime)) {
            log.info("openVIP 会员开通失败, 存在参数值为空的参数 userId: {}, type: {}, usefulTime: {}", userId, type, usefulTime);
            return ResponseData.createFailResponseData("openVIPInfo", false, "参数错误", "param_error");
        }
        User user = userDao.selectOneById(userId);
        if (ObjectUtils.isEmpty(user)) {
            log.info("openVIP 会员开通失败, 不存在此用户");
            return ResponseData.createFailResponseData("openVIPInfo", false, "不存在此用户", "user_not_exists");
        }
        Integer days = 0;
        Integer vipType = 0;
        Long nowTime = System.currentTimeMillis();
        try {
            days = Integer.parseInt(usefulTime);
            vipType = Integer.parseInt(type);
        } catch (NumberFormatException e) {
            log.info("openVIP 有效时间参数有误, usefulTime: {}, type: {}", usefulTime, type);
            return ResponseData.createFailResponseData("openVIPInfo", false, "参数错误", "param_error");
        }
        userDao.updateVIP(userId, 1, nowTime, nowTime + days * 24 * 3600 * 1000L, vipType, nowTime);
        log.info("openVIP 会员开通成功");
        return ResponseData.createSuccessResponseData("openVIPInfo", true);
    }

    /**
     * 设置会员是否自动续费
     * @param userId 用户 id
     * @param automaticRenewal 是否自动续费
     * @return 返回设置是否成功
     */
    public ResponseData setAutomaticRenewal(Integer userId, Integer automaticRenewal) {
        User user = userDao.selectOneById(userId);
        if (user == null) {
            log.info("setAutomaticRenewal 会员是否自动续费设置失败, 用户不存在 userId: {}", userId);
            return ResponseData.createFailResponseData("setAutomaticRenewalInfo", false, "用户不存在", "user_is_not_exists");
        }
        if (user.getIsVIP() == 0) {
            log.info("setAutomaticRenewal 会员是否自动续费设置失败, 用户未开通会员 user: {}", user);
            return ResponseData.createFailResponseData("setAutomaticRenewalInfo", false, "用户未开通会员", "user_is_not_vip");
        }
        userDao.updateAutomaticRenewal(userId, automaticRenewal);
        log.info("setAutomaticRenewal 会员是否自动续费设置成功 userId: {}", userId);
        return ResponseData.createSuccessResponseData("setAutomaticRenewalInfo", true);
    }

    /**
     * 会员续费
     * @param userId 用户 id
     * @param paramsMap 参数 -- 会员类型、有效时间
     * @return 返回续费是否成功
     */
    public ResponseData renewalVIP(Integer userId, Map<String, String> paramsMap) {
        String usefulTime = paramsMap.get("usefulTime"); // 有效时间
        if (ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(usefulTime)) {
            log.info("renewalVIP 会员开通失败, 存在参数值为空的参数 userId: {}, usefulTime: {}", userId, usefulTime);
            return ResponseData.createFailResponseData("renewalVIPInfo", false, "参数错误", "param_error");
        }
        User user = userDao.selectOneById(userId);
        if (ObjectUtils.isEmpty(user)) {
            log.info("renewalVIP 会员续费失败, 不存在此用户, userId: {}", userId);
            return ResponseData.createFailResponseData("renewalVIPInfo", false, "不存在此用户", "user_not_exists");
        }
        Integer days = 0;
        Long nowTime = System.currentTimeMillis();
        try {
            days = Integer.parseInt(usefulTime);
        } catch (NumberFormatException e) {
            log.info("renewalVIP 有效时间参数有误, usefulTime: {}", usefulTime);
            return ResponseData.createFailResponseData("renewalVIPInfo", false, "参数错误", "param_error");
        }
        Long vipCreateTime = user.getVipCreateTime() == 0 ? nowTime : user.getVipCreateTime();
        userDao.updateVIP(userId, 1, vipCreateTime, nowTime + days * 24 * 3600 * 1000L, user.getVipType(), System.currentTimeMillis());
        log.info("renewalVIP 会员续费成功");
        return ResponseData.createSuccessResponseData("renewalVIPInfo", true);
    }

    /**
     * 会员到期
     * @param userId 用户 id
     */
    public void closeVIP(Integer userId) {
        log.info("closeVIP 会员到期, userId: {}", userId);
        User user = userDao.selectOneById(userId);
        userDao.updateVIP(userId, -1, 0L, user.getVipEndTime(), -1, System.currentTimeMillis());
    }

}
