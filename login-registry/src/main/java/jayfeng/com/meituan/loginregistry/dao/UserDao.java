package jayfeng.com.meituan.loginregistry.dao;

import jayfeng.com.meituan.loginregistry.bean.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 美团用户数据持久层
 * @author JayFeng
 * @date 2021/2/1
 */
@Repository
public interface UserDao {

    @Insert("INSERT INTO user(`user_name`, `phone`, `password`, `create_time`, `update_time`) " +
            "VALUES(#{user.userName}, #{user.phone}, #{user.password}, #{user.createTime}, #{user.updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertUser(@Param("user") User user);

    @Delete("DELETE FROM user WHERE `id` = #{id}")
    void deleteUserById(@Param("id") Integer id);

    @Update("UPDATE user SET `password` = #{password}, `update_time` = #{updateTime} WHERE `phone` = #{phone}")
    void updateUserPasswordByPhone(@Param("phone") String phone, @Param("password") String password, @Param("updateTime") Long updateTime);

    @Update("UPDATE user SET `user_name` = #{user.userName} WHERE `id` = #{user.id}")
    void updateUserNameById(@Param("user") User user);

    @Select("SELECT `id`, `user_name`, `password`, `head_image`, `is_vip`, `vip_grade` FROM user WHERE `phone` = #{phone}")
    User selectOneByPhone(@Param("phone") String phone);

    @Select("SELECT `id`, `user_name`, `head_image`, `is_vip`, `vip_grade` FROM user WHERE `user_name` = #{userName}")
    User selectOneByUserName(@Param("userName") String userName);

    @Select("SELECT `id`, `user_name`, `head_image`, `is_vip`, `vip_grade` FROM user WHERE `email` = #{email}")
    User selectOneByEmail(@Param("email") String email);

    @Select("SELECT `id`, `user_name`, `head_image`, `is_vip`, `vip_grade` FROM user WHERE `phone` = #{phone} AND `password` = #{password}")
    User selectOneByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password);

    @Select("SELECT `id` FROM WHERE `id_card` = #{idCard}")
    Integer selectOneByIdCard(@Param("idCard") String idCard);

    @Select("SELECT `id`, `phone`, `update_time` FROM user WHERE `phone` = #{phone}")
    User selectOneUpdateTimeByPhone(@Param("phone") String phone);

    @Select("SELECT `id`, `phone`, `update_time` FROM user WHERE `user_name` = #{userName}")
    User selectOneUpdateTimeByUserName(@Param("userName") String userName);

    @Select("SELECT `id`, `phone`, `update_time` FROM user WHERE `email` = #{email}")
    User selectOneUpdateTimeByEmail(@Param("email") String email);


    @Select("SELECT * FROM user ")
    List<User> selectAll();

}
