package jayfeng.com.meituan.personalmessage.dao;

import jayfeng.com.meituan.personalmessage.bean.Coupon;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户优惠券数据持久层
 * @author JayFeng
 * @date 2021/2/23
 */
@Repository
public interface CouponDao {

    @Insert("INSERT INTO coupon(`user_id`, `money`, `coupon_name`, `use_rule`, `valid_time`, `status`, `create_time`, `update_time`) " +
            "VALUES(#{coupon.userId}, #{coupon.money}, #{coupon.couponName}, #{useRule}, #{validTime}, #{status}, #{createTime}, #{updateTime})")
    void insertCoupon(@Param("coupon") Coupon coupon);

    @Select("SELECT `id`, `user_id`, `money`, `coupon_name`, `use_rule`, `valid_time`, `status` FROM coupon WHERE `id` = #{id}")
    Coupon findCouponById(@Param("id") Integer id);

    @Select("SELECT `id`, `user_id`, `money`, `coupon_name`, `use_rule`, `valid_time`, `status` FROM coupon WHERE `user_id` = #{userId} AND `status` = #{status}")
    List<Coupon> findAllCouponByUserId(@Param("userId") Integer userId, @Param("status") Integer status);

    @Update("UPDATE FROM coupon SET `status` = #{status} WHERE `id` = #{id}")
    void updateCouponStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Delete("DELETE FROM coupon WHERE `id` = #{id} AND `status` = #{status}")
    void deleteOneUnValidCoupon(@Param("id") Integer id, @Param("status") Integer status);

    @Delete("DELETE FROM coupon WHERE `user_id` = #{userId} AND `status` = #{status}")
    void deleteAllUnValidCoupons(@Param("userId") Integer userId, @Param("status") Integer status);
}
