package jayfeng.com.meituan.loginregistry.controller;

import jayfeng.com.meituan.loginregistry.bean.AccessKey;
import jayfeng.com.meituan.loginregistry.response.ResponseMessage;
import jayfeng.com.meituan.loginregistry.service.AccessKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 验证码密钥
 * @author JayFeng
 * @date 2021/3/27
 */
@Slf4j
@RequestMapping("/meituan/user/login_registry/accessKey")
public class AccessKeyController extends BaseController {

    @Autowired
    private AccessKeyService accessKeyService;

    /**
     * 获取所有短信密钥
     * @return 返回获取所有短信密钥
     */
    @GetMapping("/findAllAccessKey")
    public ResponseMessage findAllAccessKey() {
        log.info("findAllAccessKey 获取所有短信服务密钥");
        return requestSuccess(accessKeyService.findAllAccessKey());
    }

    /**
     * 根据用途获取短信密钥
     * @param type 用来做什么 0 代表短信验证码
     * @return 返回数据
     */
    @GetMapping("/findAccessKeysByType/{type}")
    public ResponseMessage findAccessKeysByType(@PathVariable("type") Integer type) {
        log.info("findAccessKeysByType 获取某一类短信密钥 type: {}", type);
        return requestSuccess(accessKeyService.findAccessKeysByType(type));
    }

    /**
     * 根据 id 修改短信服务密钥
     * @param newAccessKey 密钥
     * @return 返回修改是否成功
     */
    @PutMapping("/updateAccessKeyById")
    public ResponseMessage updateAccessKeyById(@RequestBody AccessKey newAccessKey) {
        log.info("updateAccessKeyById 修改短信服务密钥 newAccessKey: {}", newAccessKey);
        return requestSuccess(accessKeyService.updateAccessKeyById(newAccessKey));
    }

    /**
     * 删除短信服务密钥
     * @param id id
     * @return 返回删除是否成功
     */
    @DeleteMapping("/deleteAccessKeyById/{id}")
    public ResponseMessage deleteAccessKeyById(@PathVariable("id") Integer id) {
        log.info("deleteAccessKeyById 删除短信服务密钥 id: {}", id);
        return requestSuccess(accessKeyService.deleteAccessKeyById(id));
    }

}
