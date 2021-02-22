package jayfeng.com.meituan.personalmessage.dao;

import jayfeng.com.meituan.personalmessage.bean.DeliveryAddress;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收货地址数据持久层
 * @author JayFeng
 * @date 2021/2/8
 */
@Repository
public interface DeliveryAddressDao {

    @Insert("INSERT INTO delivery_address(`user_id`, `address`, `door_code`, `name`, `sex`, `phone`, `tag`, `is_default`, `update_time`, `create_time`) VALUES(#{deliveryAddress.userId}, #{deliveryAddress.address}, #{deliveryAddress.doorCode}, #{deliveryAddress.name}, #{deliveryAddress.sex} #{deliveryAddress.phone}, #{deliveryAddress.tag}, #{deliveryAddress.isDefault}, #{deliveryAddress.createTime}, #{deliveryAddress.updateTime})")
    void insertDeliveryAddress(@Param("deliveryAddress") DeliveryAddress deliveryAddress);

    @Delete("DELETE FROM delivery_address WHERE `id` = #{id}")
    void deleteDeliveryAddressById(@Param("id") Integer id);

    @Update("UPDATE FROM delivery_address SET `address` = #{deliveryAddress.address}, `door_code` = #{deliveryAddress.doorCode}, `name` = #{deliveryAddress.name}, `sex` = #{deliveryAddress.sex}, `phone` = #{deliveryAddress.phone}, `tag` = #{deliveryAddress.tag}, `update_time` = #{deliveryAddress.updateTime} WHERE `id` = #{deliveryAddress.id}")
    void updateDeliveryAddressMessage(@Param("deliveryAddress") DeliveryAddress deliveryAddress, @Param("updateTime") Long updateTime);

    @Update("UPDATE FROM delivery_address SET `is_default` = 0, `update_time` = #{updateTime} WHERE `user_id` = #{userId}")
    void cancelDefaultAddress(@Param("userId") Integer userId, @Param("updateTime") Long updateTime);

    @Update("UPDATE FROM delivery_address SET `is_default` = 1, `update_time` = #{updateTime} WHERE `id` = #{deliveryAddressId}")
    void updateDefaultAddress(@Param("deliveryAddressId") Integer deliveryAddressId, @Param("updateTime") Long updateTime);

    @Select("SELECT * FROM delivery_address WHERE `id` = #{id}")
    DeliveryAddress selectOneById(@Param("id") Integer id);

    @Select("SELECT * FROM delivery_address WHERE `user_id` = #{userId} AND `is_default` = 1")
    DeliveryAddress selectDefaultDeliveryAddress(@Param("user_id") Integer userId);

    @Select("SELECT * FROM delivery_address WHERE `user_id` = #{userId}")
    List<DeliveryAddress> selectUserAllDeliveryAddress(@Param("userId") Integer userId);

}
