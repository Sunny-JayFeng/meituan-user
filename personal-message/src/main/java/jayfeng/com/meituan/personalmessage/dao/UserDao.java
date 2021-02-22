package jayfeng.com.meituan.personalmessage.dao;

import jayfeng.com.meituan.personalmessage.bean.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author JayFeng
 * @date 2021/2/12
 */
@Repository
public interface UserDao {

    // 这个只是用来判断是否存在这个用户
    @Select("SELECT `id` FROM user WHERE `id` = #{userId}")
    Integer selectIdById(@Param("userId") Integer userId);

    @Select("SELECT `user_name`, `head_image`, `is_vip`, `automatic_renewal` `vip_create_time`, `vip_end_time`, `vip_type`, `vip_grade` FROM user WHERE `id` = #{userId}")
    User selectOneById(@Param("userId") Integer userId);

    @Select("SELECT `id` FROM user WHERE `id` = #{userId} AND `user_name` = #{userName}")
    User selectOneByIdAndUserName(@Param("userId") Integer userId, @Param("userName") String userName);

    @Select("SELECT `id` FROM user WHERE `id` = #{userId} AND `password` = #{password}")
    User selectOneByIdAndPassword(@Param("userId") Integer userId, @Param("password") String password);

    @Update("UPDATE FROM user SET `head_image` = #{headImage}, `update_time` = #{updateTime} WHERE `id` = #{userId}")
    void updateHeadImage(@Param("userId") Integer userId, @Param("headImage") String headImage, @Param("updateTime") Long updateTime);

    @Update("UPDATE FROM user SET `user_name` = #{userName} `update_time` = #{updateTime} WHERE `id` = #{userId}")
    void updateUserName(@Param("userId") Integer userId, @Param("userName") String userName, @Param("updateTime") Long updateTime);

    @Update("UPDATE FROM user SET `password` = #{password} `update_time` = #{updateTime} WHERE `id` = #{userId}")
    void updatePassword(@Param("userId") Integer userId, @Param("password") String password, @Param("updateTime") Long updateTime);

    @Update("UPDATE FROM user SET `birthday` = #{birthday}, `update_time` = #{updateTime} WHERE `id` = #{userId}")
    void updateBirthday(@Param("userId") Integer userId, @Param("birthday") Long birthday, @Param("updateTime") Long updateTime);

    @Update("UPDATE FROM user SET `is_vip` = #{isVIP}, `vip_create_time` = #{vipCreateTime}, `vip_end_time` = #{vipEndTime}, `vip_type` = #{vipType}, `update_time` = #{updateTime} WHERE `id` = #{userId}")
    void updateVIP(@Param("userId") Integer userId, @Param("isVIP") Integer isVIP, @Param("vipCreateTime") Long vipCreateTime, @Param("vipEndTime") Long vipEndTime, @Param("vipType") Integer vipType, @Param("updateTime") Long updateTime);

    @Update("UPDATE FROM user SET `automatic_renewal` = #{automaticRenewal} WHERE `id` = #{userId}")
    void updateAutomaticRenewal(@Param("userId") Integer userId, @Param("automaticRenewal") Integer automaticRenewal);

}
