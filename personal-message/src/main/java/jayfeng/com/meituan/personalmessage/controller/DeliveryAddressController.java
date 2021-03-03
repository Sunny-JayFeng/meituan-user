package jayfeng.com.meituan.personalmessage.controller;

import jayfeng.com.meituan.personalmessage.bean.DeliveryAddress;
import jayfeng.com.meituan.personalmessage.response.ResponseMessage;
import jayfeng.com.meituan.personalmessage.service.DeliveryAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收货地址控制层
 * @author JayFeng
 * @date 2021/2/8
 */
@RestController
@RequestMapping("/meituan/user")
@Slf4j
public class DeliveryAddressController extends BaseController {

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    /**
     * 根据用户 id 获取用户所有的收货地址
     * @param userId 用户 id
     * @return 返回用户所有的收货地址
     */
    @GetMapping("/getUserAllDeliveryAddress/{userId}")
    public ResponseMessage getUserAllDeliveryAddress(@PathVariable("userId") Integer userId) {
        log.info("getUserAllDeliveryAddress 获取用户所有收货地址, userId: {}", userId);
        return requestSuccess(deliveryAddressService.getUserAllDeliveryAddress(userId));
    }

    /**
     * 根据用户 id 获取默认收货地址
     * @param userId 用户 id
     * @return 返回默认收货地址
     */
    @GetMapping("/getDefaultAddress{userId}")
    public ResponseMessage getDefaultAddress(@PathVariable("userId") Integer userId) {
        log.info("getDefaultAddress 获取用户默认收货地址, userId: {}", userId);
        return requestSuccess(deliveryAddressService.getDefaultAddress(userId));
    }

    /**
     * 添加一个收货地址
     * @param deliveryAddress 收货地址
     * @return 返回添加是否成功
     */
    @PostMapping("/addDeliveryAddress")
    public ResponseMessage addDeliveryAddress(@RequestBody DeliveryAddress deliveryAddress) {
        log.info("addDeliveryAddress 添加收货地址, deliveryAddress: {}", deliveryAddress);
        return requestSuccess(deliveryAddressService.addDeliveryAddress(deliveryAddress));
    }

    /**
     * 根据 id 删除一个收货地址
     * @param id 收货地址 id
     * @return 返回删除是否成功
     */
    @DeleteMapping("/removeDeliveryAddress/{id}")
    public ResponseMessage removeDeliveryAddress(@PathVariable("id") Integer id) {
        log.info("removeDeliveryAddress 删除收货地址, id: {}", id);
        return requestSuccess(deliveryAddressService.removeDeliveryAddress(id));
    }

    /**
     * 修改收货地址
     * @param deliveryAddress 收货地址
     * @return 返回修改是否成功
     */
    @PutMapping("/changeDeliveryAddress")
    public ResponseMessage changeDeliveryAddress(@RequestBody DeliveryAddress deliveryAddress) {
        log.info("changeDeliveryAddress 修改收货地址, deliveryAddress: {}", deliveryAddress);
        return requestSuccess(deliveryAddressService.changeDeliveryAddress(deliveryAddress));
    }

    /**
     * 设置默认收货地址
     * @param userId 用户 id
     * @param deliveryAddressId 收货地址 id
     * @return 返回设置是否成功
     */
    @PutMapping("/setDefaultDeliveryAddress/{userId}/{deliveryAddressId}")
    public ResponseMessage setDefaultDeliveryAddress(@PathVariable("userId") Integer userId, @PathVariable("deliveryAddressId") Integer deliveryAddressId) {
        log.info("setDefaultDeliveryAddress 设置默认收货地址, userId: {}, deliveryAddressId: {}", userId, deliveryAddressId);
        return requestSuccess(deliveryAddressService.setDefaultDeliveryAddress(userId, deliveryAddressId));
    }

}
