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

    /**
     * 新增一个用户对象
     * @param user 用户
     */
    @Insert("INSERT INTO `user`(`user_name`, `phone`, `password`, `create_time`, `update_time`) " +
            "VALUES(#{user.userName}, #{user.phone}, #{user.password}, #{user.createTime}, #{user.updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertUser(@Param("user") User user);

    /**
     * 根据用户 id 设置账号是否可用
     * @param userId 用户 id
     */
    @Update("UPDATE `user` SET `is_valid` = #{isValid} WHERE `id` = #{userId}")
    void updateUserIsValid(@Param("userId") Integer userId, @Param("isValid") Integer isValid);

    /**
     * 修改密码
     * @param phone 手机号
     * @param password 密码
     * @param updateTime 更新时间
     */
    @Update("UPDATE `user` SET `password` = #{password}, `update_time` = #{updateTime} " +
            "WHERE `phone` = #{phone}")
    void updateUserPasswordByPhone(@Param("phone") String phone, @Param("password") String password, @Param("updateTime") Long updateTime);

    /**
     * 修改用户名
     * @param user 用户对象
     */
    @Update("UPDATE `user` SET `user_name` = #{user.userName} WHERE `id` = #{user.id}")
    void updateUserNameById(@Param("user") User user);

    /**
     * 根据用户 id 查找用户
     * @param userId 用户 id
     * @return 返回用户对象
     */
    @Select("SELECT * FROM `user` WHERE `id` = #{userId}")
    User selectOneById(@Param("userId") Integer userId);

    /**
     * 根据手机号查询一个用户
     * @param phone 手机号
     * @return 返回用户对象
     */
    @Select("SELECT `id`, `user_name`, `head_image`, `is_vip`, `automatic_renewal`, " +
            "`vip_create_time`, `vip_end_time`, `vip_type`, `vip_grade`, `birthday`, `is_valid` " +
            "FROM `user` WHERE `phone` = #{phone}")
    User selectOneByPhone(@Param("phone") String phone);

    /**
     * 根据用户名查询一个用户
     * @param userName 用户名
     * @return 返回用户对象
     */
    @Select("SELECT `id`, `user_name`, `head_image`, `is_vip`, `automatic_renewal`, " +
            "`vip_create_time`, `vip_end_time`, `vip_type`, `vip_grade`, `birthday`, `is_valid` " +
            "FROM `user` WHERE `user_name` = #{userName}")
    User selectOneByUserName(@Param("userName") String userName);

    /**
     * 根据邮箱查询一个用户
     * @param email 邮箱
     * @return 返回用户对象
     */
    @Select("SELECT `id`, `user_name`, `head_image`, `is_vip`, `automatic_renewal`, " +
            "`vip_create_time`, `vip_end_time`, `vip_type`, `vip_grade`, `birthday`, `is_valid` " +
            "FROM `user` WHERE `email` = #{email}")
    User selectOneByEmail(@Param("email") String email);

    /**
     * 查询身份证是否已经被绑定
     * @param idCard 身份证号
     * @return 返回 id
     */
    @Select("SELECT `id` FROM `user` WHERE `id_card` = #{idCard}")
    Integer selectOneByIdCard(@Param("idCard") String idCard);

    /**
     * 根据手机号查询一个用户(只要id、phone、updateTime，不需要完整对象)
     * @param phone 手机号
     * @return 返回用户对象
     */
    @Select("SELECT `id`, `phone`, `update_time` FROM `user` WHERE `phone` = #{phone}")
    User selectOneUpdateTimeByPhone(@Param("phone") String phone);

    /**
     * 根据用户名查询一个用户(只要id、phone、updateTime，不需要完整对象)
     * @param userName 用户名
     * @return 返回用户对象
     */
    @Select("SELECT `id`, `phone`, `update_time` FROM `user` WHERE `user_name` = #{userName}")
    User selectOneUpdateTimeByUserName(@Param("userName") String userName);

    /**
     * 根据邮箱查询一个用户(只要id、phone、updateTime，不需要完整对象)
     * @param email 邮箱
     * @return 返回用户对象
     */
    @Select("SELECT `id`, `phone`, `update_time` FROM `user` WHERE `email` = #{email}")
    User selectOneUpdateTimeByEmail(@Param("email") String email);


    @Select("SELECT * FROM `user` ")
    List<User> selectAll();

}
