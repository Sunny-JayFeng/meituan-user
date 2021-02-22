package jayfeng.com.meituan.personalmessage.controller;

import jayfeng.com.meituan.personalmessage.response.ResponseMessage;
import jayfeng.com.meituan.personalmessage.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author JayFeng
 * @date 2021/2/12
 */
@RestController
@RequestMapping("/meituan/user")
public class UserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 修改用户头像
     * @param userId 用户 id
     * @param headImage 用户头像
     * @return 返回修改是否成功
     */
    @PutMapping("/changeHeadImage/{userId}")
    public ResponseMessage changeHeadImage(@PathVariable("userId") Integer userId, @RequestParam("headImage") String headImage) {
        logger.info("changeHeadImage 修改用户头像 userId: {}, headImage: {}", userId, headImage);
        return requestSuccess(userService.changeHeadImage(userId, headImage));
    }

    /**
     * 修改用户昵称
     * @param userId 用户 id
     * @param paramsMap 请求参数 -- 旧昵称 新昵称
     * @return 返回修改是否成功
     */
    @PutMapping("/changeUserName/{userId}")
    public ResponseMessage changeUserName(@PathVariable("userId") Integer userId, @RequestBody Map<String, String> paramsMap) {
        logger.info("changeUserName 修改用户昵称 userId: {}, paramsMap: {}", userId, paramsMap);
        return requestSuccess(userService.changeUserName(userId, paramsMap));
    }

    /**
     * 修改密码
     * @param userId 用户id
     * @param paramsMap 请求参数 -- 旧密码 新密码 验证码
     * @return 返回修改是否成功
     */
    @PutMapping("/changePassword/{userId}")
    public ResponseMessage changePassword(@PathVariable("userId") Integer userId, @RequestBody Map<String, String> paramsMap) {
        logger.info("changePassword 修改密码 userId: {}, paramsMap: {}", userId, paramsMap);
        return requestSuccess(userService.changePassword(userId, paramsMap));
    }

    /**
     * 修改生日
     * @param userId 用户id
     * @param birthday 生日
     * @return 返回修改是否成功
     */
    @PutMapping("/changeBirthday/{userId}")
    public ResponseMessage changeBirthday(@PathVariable("userId") Integer userId, @RequestParam("birthday") Long birthday) {
        logger.info("changeBirthday 修改生日 userId: {}, birthday: {}", userId, birthday);
        return requestSuccess(userService.changeBirthday(userId, birthday));
    }

    /**
     * 开通会员
     * @param userId 用户 id
     * @param paramsMap 参数 -- 会员类型、有效时间
     * @return 返回开通是否成功
     */
    @PutMapping("/openVIP/{userId}")
    public ResponseMessage openVIP(@PathVariable("userId") Integer userId, @RequestBody Map<String, String> paramsMap) {
        logger.info("openVIP 开通会员 userId: {}, paramsMap: {}", userId, paramsMap);
        return requestSuccess(userService.openVIP(userId, paramsMap));
    }

    /**
     * 设置会员是否自动续费
     * @param userId 用户 id
     * @param automaticRenewal 是否自动续费
     * @return 返回设置是否成功
     */
    @PutMapping("/automaticRenewal/{userId}/{automaticRenewal}")
    public ResponseMessage setAutomaticRenewal(@PathVariable("userId") Integer userId, @PathVariable("automaticRenewal") Integer automaticRenewal) {
        logger.info("setAutomaticRenewal 修改会员是否自动续费 userId: {}, automaticRenewal: {}", userId, automaticRenewal);
        return requestSuccess(userService.setAutomaticRenewal(userId, automaticRenewal));
    }

    /**
     * 会员续费
     * @param userId 用户id
     * @param paramsMap 参数 -- 会员类型、有效时间
     * @return 返回续费是否成功
     */
    @PutMapping("/renewalVIP/{userId}")
    public ResponseMessage renewalVIP(@PathVariable("userId") Integer userId, @RequestBody Map<String, String> paramsMap) {
        logger.info("renewalVIP 会员续费, userId: {}, paramsMap: {}", userId, paramsMap);
        return requestSuccess(userService.renewalVIP(userId, paramsMap));
    }

}
