package jayfeng.com.meituan.loginregistry.dao;

import jayfeng.com.meituan.loginregistry.bean.AccessKey;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 验证码密钥
 * @author JayFeng
 * @date 2021/3/27
 */
@Repository
public interface AccessKeyDao {

    @Select("SELECT `id`, `region_id`, `access_key_id`, `secret` FROM `access_key`")
    List<AccessKey> selectAllAccessKey();

    @Select("SELECT `id`, `region_id`, `access_key_id`, `secret` FROM `access_key` WHERE `type` = #{type}")
    List<AccessKey> selectAccessKeyList(@Param("type") Integer type);

    @Select("SELECT `id`, `region_id`, `access_key_id`, `secret`, `create_time`, `update_time` FROM `access_key` WHERE `id` = #{id}")
    AccessKey selectAccessKey(@Param("id") Integer id);

    @Delete("DELETE FROM `access_key` WHERE `id` = #{id}")
    void deleteAccessKey(@Param("id") Integer id);

    @Update("UPDATE `access_key` SET `region_id` = #{accessKey.regionId}, `access_key_id` = #{accessKey.accessKeyId}, " +
            "`secret` = #{accessKey.secret}, `update_time` = #{accessKey.updateTime} WHERE `id` = #{accessKey.id}")
    void updateAccessKey(@Param("accessKey") AccessKey accessKey);
}
