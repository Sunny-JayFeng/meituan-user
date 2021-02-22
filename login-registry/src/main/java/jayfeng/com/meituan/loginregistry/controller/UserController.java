package jayfeng.com.meituan.loginregistry.controller;

import jayfeng.com.meituan.loginregistry.response.ResponseMessage;
import jayfeng.com.meituan.loginregistry.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/meituan/user")
public class UserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户通过 手机号 + 验证码 登录
     * @param paramsMap 参数
     * @param response 响应
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/loginByCode")
    public ResponseMessage loginByCode(@RequestBody Map<String, String> paramsMap, HttpServletResponse response) {
        logger.info("loginByCode, paramsMap: {}", paramsMap);
        return requestSuccess(userService.loginByCode(paramsMap, response));
    }

    /**
     * 获取短信验证码
     * @param phone 手机号
     * @return 返回 ResponseMessage 对象
     */
    @GetMapping("/getIdentifyCode/{phone}")
    public ResponseMessage getIdentifyCode(@PathVariable("phone") String phone) {
        logger.info("getIdentifyCode, phone: {}", phone);
        return requestSuccess(userService.sendIdentifyCode(phone));
    }

    /**
     * 检查输入的验证码是否正确
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @GetMapping("/checkIdentifyCode")
    public ResponseMessage checkIdentifyCode(@RequestBody Map<String, String> paramsMap) {
        logger.info("checkIdentifyCode, paramsMap: {}", paramsMap);
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
        logger.info("loginByPassword, paramsMap: {}", paramsMap);
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
        logger.info("userLogout");
        return requestSuccess(userService.userLogout(request, response));
    }

    /**
     * 用户注册
     * @param registryMessage 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/registry")
    public ResponseMessage userRegistry(@RequestBody Map<String, String> registryMessage) {
        logger.info("userRegistry, registryMessage: {}", registryMessage);
        return requestSuccess(userService.userRegistry(registryMessage));
    }

    /**
     * 注销一个用户账号
     * @param id 用户 id
     * @return 返回 ResponseMessage 对象
     */
    @DeleteMapping("/closeUserAccount/{id}")
    public ResponseMessage closeUserAccount(@PathVariable("id") Integer id) {
        logger.info("closeUserAccount, id: {}", id);
        return requestSuccess(userService.closeUserAccount(id));
    }

    /**
     * 修改密码
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/changePassword")
    public ResponseMessage changePassword(@RequestBody Map<String, String> paramsMap) {
        logger.info("changePassword, paramsMap: {}", paramsMap);
        return requestSuccess(userService.changePassword(paramsMap));
    }

    /**
     * 忘记密码 -- 重置密码 -- 校验账号的安全性
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/checkAccountSafe")
    public ResponseMessage checkAccountSafe(@RequestBody Map<String, String> paramsMap) {
        logger.info("checkAccountSafe, paramsMap: {}", paramsMap);
        return requestSuccess(userService.checkAccountSafe(paramsMap));
    }

    /**
     * 二次验证页面获取验证码时，需要先判断所携带的 ticket 是否有效
     * @param phone 手机号
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/checkExistsTicket/{phone}")
    public ResponseMessage checkExistsTicket(@PathVariable String phone) {
        logger.info("checkExistsTicket, phone: {}", phone);
        return requestSuccess(userService.checkExistsTicket(phone));
    }

    /**
     * 忘记密码 -- 重置密码 -- 需要二次校验 -- 校验验证码是否正确
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/verifyCheckIdentifyCode")
    public ResponseMessage verifyCheckIdentifyCode(@RequestBody Map<String, String> paramsMap) {
        logger.info("verifyCheckIdentifyCode, paramsMap: {}", paramsMap);
        return requestSuccess(userService.verifyCheckIdentifyCode(paramsMap));
    }

    /**
     * 忘记密码 -- 重置密码
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PutMapping("/retrievePassword")
    public ResponseMessage retrievePassword(@RequestBody Map<String, String> paramsMap) {
        logger.info("retrievePassword, paramsMap: {}", paramsMap);
        return requestSuccess(userService.retrievePassword(paramsMap));
    }

    /**
     * 校验用户电话的重复性
     * @param phone 手机号
     * @return 返回 ResponseMessage 对象
     */
    @GetMapping("/checkPhoneExist/{phone}")
    public ResponseMessage checkPhoneExist(@PathVariable("phone") String phone) {
        logger.info("checkPhoneExist, phone: {}", phone);
        return requestSuccess(userService.checkPhoneExist(phone));
    }

    /**
     * 校验用户身份证的重复性
     * @param idCard 身份证
     * @return 返回 ResponseMessage 对象
     */
    @GetMapping("/checkIdCardExist/{idCard}")
    public ResponseMessage checkIdCardExist(@PathVariable("idCard") String idCard) {
        logger.info("checkIdCardExist, idCard: {}", idCard);
        return requestSuccess(userService.checkIdCardExist(idCard));
    }

    /**
     * 校验用户名是否存在
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/checkUserNameExist")
    public ResponseMessage checkUserNameExist(@RequestBody Map<String, String> paramsMap) {
        logger.info("checkUserNameExist, paramsMap: {}", paramsMap);
        return requestSuccess(userService.checkUserNameExist(paramsMap));
    }

    /**
     * 检验邮箱是否绑定某个用户
     * @param paramsMap 请求参数
     * @return 返回 ResponseMessage 对象
     */
    @PostMapping("/checkEmailExist")
    public ResponseMessage checkEmailExist(@RequestBody Map<String, String> paramsMap) {
        logger.info("checkEmailExist, paramsMap: {}", paramsMap);
        return requestSuccess(userService.checkEmailExist(paramsMap));
    }
}
