package jayfeng.com.meituan.loginregistry.dao;

import jayfeng.com.meituan.loginregistry.bean.AccessKey;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 验证码密钥
 * @author JayFeng
 * @date 2021/3/27
 */
@Repository
public interface AccessKeyDao {

    /**
     * 新增一个密钥
     * @param accessKey 密钥
     */
    @Insert("INSERT INTO `access_key`(`region_id`, `access_key_id`, `secret`, `type`, `create_time`, `update_time`) " +
            "VALUES(#{accessKey.regionId}, #{accessKey.accessKeyId}, #{accessKey.secret}, #{accessKey.type}, #{accessKey.createTime}, #{accessKey.updateTime})")
    void addAccessKey(@Param("accessKey") AccessKey accessKey);

    /**
     * 查询所有密钥
     * @return 返回密钥列表
     */
    @Select("SELECT `id`, `region_id`, `access_key_id`, `secret`, `type` FROM `access_key`")
    List<AccessKey> selectAllAccessKey();

    /**
     * 根据类型查询密钥
     * @param type 类型
     * @return 返回密钥列表
     */
    @Select("SELECT `id`, `region_id`, `access_key_id`, `secret`, `type` FROM `access_key` WHERE `type` = #{type}")
    List<AccessKey> selectAccessKeyList(@Param("type") Integer type);

    /**
     * 根据 id 查询密钥
     * @param id id
     * @return 返回密钥
     */
    @Select("SELECT `id`, `region_id`, `access_key_id`, `secret`, `type`, `create_time`, `update_time` FROM `access_key` WHERE `id` = #{id}")
    AccessKey selectAccessKey(@Param("id") Integer id);

    /**
     * 检查当前 accessKeyId 是否已经存在
     * @param accessKeyId accessKeyId
     * @return 返回 id
     */
    @Select("SELECT `id`, `region_id`, `access_key_id`, `secret`, `type`, `create_time`, `update_time` FROM `access_key` WHERE `access_key_id` = #{accessKeyId}")
    AccessKey selectOneByAccessKeyId(@Param("accessKeyId") String accessKeyId);

    /**
     * 根据 id 删除密钥
     * @param id id
     */
    @Delete("DELETE FROM `access_key` WHERE `id` = #{id}")
    void deleteAccessKey(@Param("id") Integer id);

    /**
     * 根据 id 更新密钥信息
     * @param accessKey 密钥
     */
    @Update("UPDATE `access_key` SET `region_id` = #{accessKey.regionId}, `access_key_id` = #{accessKey.accessKeyId}, " +
            "`secret` = #{accessKey.secret}, `update_time` = #{accessKey.updateTime} WHERE `id` = #{accessKey.id}")
    void updateAccessKey(@Param("accessKey") AccessKey accessKey);
}
