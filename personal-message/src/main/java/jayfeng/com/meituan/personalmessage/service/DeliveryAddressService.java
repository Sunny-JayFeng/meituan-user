package jayfeng.com.meituan.personalmessage.service;

import jayfeng.com.meituan.personalmessage.bean.DeliveryAddress;
import jayfeng.com.meituan.personalmessage.dao.DeliveryAddressDao;
import jayfeng.com.meituan.personalmessage.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 收货地址业务逻辑层
 * @author JayFeng
 * @date 2021/2/8
 */
@Service
@Slf4j
public class DeliveryAddressService {

    @Autowired
    private DeliveryAddressDao deliveryAddressDao;


    /**
     * 根据用户 id 获取用户所有的收货地址
     * @param userId 用户 id
     * @return 返回用户所有的收货地址
     */
    public ResponseData getUserAllDeliveryAddress(Integer userId) {
        log.info("getUserAllDeliveryAddress 获取用户所有的收货地址, userId: {}", userId);
        if (ObjectUtils.isEmpty(userId)) {
            log.info("getUserAllDeliveryAddress 获取收货地址失败, 用户 id 为空");
            return ResponseData.createFailResponseData("getUserAllDeliveryAddressInfo", false, "获取收货地址失败, 无法找到用户", "can_not_find_user");
        }
        List<DeliveryAddress> deliveryAddressList = deliveryAddressDao.selectUserAllDeliveryAddress(userId);
        return ResponseData.createSuccessResponseData("getUserAllDeliveryAddressInfo", deliveryAddressList);
    }

    /**
     * 根据用户 id 获取默认收货地址
     * @param userId 用户 id
     * @return 返回默认收货地址
     */
    public ResponseData getDefaultAddress(Integer userId) {
        if (ObjectUtils.isEmpty(userId)) {
            log.info("getDefaultAddress 获取用户默认收货地址失败, 用户 id 为空");
            return ResponseData.createFailResponseData("getDefaultAddressInfo", false, "无法查找用户", "can_not_find_user");
        }
        log.info("getDefaultAddress 获取用户默认收货地址, userId: {}", userId);
        DeliveryAddress deliveryAddress = deliveryAddressDao.selectDefaultDeliveryAddress(userId);
        log.info("getDefaultAddress 获取用户默认收货地址, deliveryAddress: {}", deliveryAddress);
        return ResponseData.createSuccessResponseData("getDefaultAddressInfo", deliveryAddress);
    }


    /**
     * 添加一个收货地址 暂不考虑地址重复问题
     * @param deliveryAddress 收货地址
     * @return 返回添加是否成功
     */
    public ResponseData addDeliveryAddress(DeliveryAddress deliveryAddress) {
        log.info("addDeliveryAddress 添加收货地址, deliveryAddress: {}", deliveryAddress);
        if (ObjectUtils.isEmpty(deliveryAddress)) {
            log.info("addDeliveryAddress 添加收货地址失败, 收货地址为空");
            return ResponseData.createFailResponseData("addDeliveryAddressInfo", false, "添加收货地址失败, 收货地址为空", "the_address_is_empty");
        }
        deliveryAddressDao.insertDeliveryAddress(deliveryAddress);
        return ResponseData.createSuccessResponseData("addDeliveryAddressInfo", true);
    }

    /**
     * 根据收获地址 id 删除一个收货地址
     * @param deliveryAddressId 收货地址 id
     * @return 返回是否删除成功
     */
    public ResponseData removeDeliveryAddress(Integer deliveryAddressId) {
        log.info("removeDeliveryAddress 根据收货地址 id 删除一个收货地址, deliveryAddressId: {}", deliveryAddressId);

        if(ObjectUtils.isEmpty(deliveryAddressId)) {
            log.info("removeDeliveryAddress 收货地址删除失败, 收货地址 id 为空");
            return ResponseData.createFailResponseData("removeDeliveryAddressInfo", false, "删除失败, 找不到此收货地址", "can_not_find_this_address");
        }
        DeliveryAddress deliveryAddress = deliveryAddressDao.selectOneById(deliveryAddressId);

        // 如果不存在这个收货地址
        if (ObjectUtils.isEmpty(deliveryAddress)) {
            log.info("removeDeliveryAddress 收货地址删除失败, 不存在这个收货地址");
            return ResponseData.createFailResponseData("removeDeliveryAddressInfo", false, "不存在这个收货地址", "the_delivery_address_is_not_exists");
        }
        deliveryAddressDao.deleteDeliveryAddressById(deliveryAddressId);
        log.info("removeDeliveryAddress 收货地址删除成功");
        return ResponseData.createSuccessResponseData("removeDeliveryAddressInfo", true);
    }

    /**
     * 修改收货地址信息
     * @param deliveryAddress 收货地址
     * @return 返回是否修改成功
     */
    public ResponseData changeDeliveryAddress(DeliveryAddress deliveryAddress) {
        log.info("changeDeliveryAddress 修改收货地址, deliveryAddress: {}", deliveryAddress);
        if (ObjectUtils.isEmpty(deliveryAddress)) {
            log.info("changeDeliveryAddress 修改收货地址信息失败, 收货地址信息为空");
            return ResponseData.createFailResponseData("changeDeliveryAddressInfo", false, "修改收货地址信息失败, 收货地址信息为空", "the_address_message_is_empty");
        }
        DeliveryAddress theAddress = deliveryAddressDao.selectOneById(deliveryAddress.getId());

        // 如果不存在这个收货地址
        if (ObjectUtils.isEmpty(theAddress)) {
            log.info("changeDeliveryAddress 修改收货地址失败, 不存在当前收货地址");
            return ResponseData.createFailResponseData("changeDeliveryAddressInfo", false, "收货地址修改失败, 不存在这个收货地址", "the_address_is_not_exists");
        }

        log.info("changeDeliveryAddress 修改收货地址成功, 旧收货地址信息: {}, 新收货地址信息: {}", theAddress, deliveryAddress);
        deliveryAddressDao.updateDeliveryAddressMessage(deliveryAddress, System.currentTimeMillis());
        return ResponseData.createSuccessResponseData("changeDeliveryAddressInfo", true);
    }

    /**
     * 设置默认收货地址
     * @param userId 用户 id
     * @param deliveryAddressId 收货地址 id
     * @return 返回修改是否成功
     */
    public ResponseData setDefaultDeliveryAddress(Integer userId, Integer deliveryAddressId) {
        log.info("setDefaultDeliveryAddress 设置默认收货地址, userId: {}, deliveryAddressId: {}", userId, deliveryAddressId);

        if (ObjectUtils.isEmpty(userId)) {
            log.info("setDefaultDeliveryAddress 设置默认收货地址失败, 用户 id 为空");
            return ResponseData.createFailResponseData("setDefaultDeliveryAddressInfo", false, "设置失败, 无法找到用户", "can_not_find_user");
        }
        DeliveryAddress deliveryAddress = deliveryAddressDao.selectOneById(deliveryAddressId);
        // 如果不存在当前收货地址
        if (ObjectUtils.isEmpty(deliveryAddress)) {
            log.info("setDefaultDeliveryAddress 设置默认收货地址失败, 不存在这个收货地址");
            return ResponseData.createFailResponseData("setDefaultDeliveryAddressInfo", false, "设置失败, 不存在这个收货地址", "the_address_is_not_exists");
        }
        // 先取消原先的默认收货地址
        deliveryAddressDao.cancelDefaultAddress(userId, System.currentTimeMillis());
        // 设置默认收货地址
        deliveryAddressDao.updateDefaultAddress(deliveryAddressId, System.currentTimeMillis());
        log.info("setDefaultDeliveryAddress 设置默认收货地址成功, 当前默认收货地址: {}", deliveryAddress);
        return ResponseData.createSuccessResponseData("setDefaultDeliveryAddressInfo", true);
    }

}
