package jayfeng.com.meituan.loginregistry.service;

import jayfeng.com.meituan.loginregistry.bean.AccessKey;
import jayfeng.com.meituan.loginregistry.dao.AccessKeyDao;
import jayfeng.com.meituan.loginregistry.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 验证码密钥
 * @author JayFeng
 * @date 2021/3/27
 */
@Service
@Slf4j
public class AccessKeyService {

    @Autowired
    private AccessKeyDao accessKeyDao;

    private static Map<Integer, List<AccessKey>> accessKeyMap = null;
    private static Random random = new Random();

    /**
     * 新增一个密钥
     * @param accessKey 密钥
     * @return 返回数据
     */
    public ResponseData addAccessKey(AccessKey accessKey) {
        AccessKey oldKey = accessKeyDao.selectOneByAccessKeyId(accessKey.getAccessKeyId());
        if (oldKey != null){
            log.info("addAccessKey 密钥新增失败, 密钥已存在 oldKey: {}", oldKey);
            return ResponseData.createFailResponseData("addAccessKeyInfo", false, "密钥已存在", "access_key_id_exists");
        }
        accessKey.setCreateTime(System.currentTimeMillis());
        accessKey.setUpdateTime(accessKey.getCreateTime());
        accessKeyDao.addAccessKey(accessKey);
        log.info("addAccessKey 密钥新增成功 accessKey: {}", accessKey);
        return ResponseData.createSuccessResponseData("addAccessKeyInfo", true);
    }

    /**
     * 初始化所有短信密钥
     */
    @PostConstruct
    private void initAllAccessKeyList() {
        log.info("initAllAccessKeyList 初始化所有短信密钥");
        List<AccessKey> accessKeyList = accessKeyDao.selectAllAccessKey();
        accessKeyMap = accessKeyList.stream().collect(Collectors.groupingBy(AccessKey::getType));
    }

    /**
     * 随机获取一个密钥
     * @param useType 是用来做什么的：发送短信则类型为 0
     * @return 返回一个密钥
     */
    public AccessKey getAccessKey(Integer useType) {
        List<AccessKey> keyList = accessKeyMap.get(useType);
        log.info("getAccessKey 获取一个密钥 useType: {}", useType);
        if (keyList == null || keyList.isEmpty()) {
            log.info("getAccessKey 获取密钥失败, 密钥数据为空, useType: {}", useType);
            return null;
        }
        AccessKey accessKey = keyList.get(random.nextInt(keyList.size()));
        log.info("getAccessKey 获取到密钥 accessKey: {}", accessKey);
        return accessKey;
    }

    /**
     * 获取所有短信密钥
     * @return 返回获取所有短信密钥
     */
    public ResponseData findAllAccessKey() {
        log.info("findAllAccessKey 获取所有短信密钥 accessKeyMap: {}", accessKeyMap.size());
        return ResponseData.createSuccessResponseData("findAllAccessKeyInfo", accessKeyMap);
    }

    /**
     * 根据用途获取短信密钥
     * @param type 用来做什么 0 代表短信验证码
     * @return 返回数据
     */
    public ResponseData findAccessKeysByType(Integer type) {
        List<AccessKey> keyList = accessKeyMap.get(type);
        log.info("findAccessKeysByType 获取某一类短信密钥 keyListSize: {}", keyList.size());
        return ResponseData.createSuccessResponseData("findAccessKeysByTypeInfo", keyList);
    }

    /**
     * 根据 id 修改短信服务密钥
     * @param newAccessKey 密钥
     * @return 返回是否成功
     */
    public ResponseData updateAccessKeyById(AccessKey newAccessKey) {
        log.info("updateAccessKeyById 根据 id 修改短信服务密钥 newAccessKey: {}", newAccessKey);
        AccessKey oldAccessKey = accessKeyDao.selectAccessKey(newAccessKey.getId());
        if (oldAccessKey == null) {
            log.info("updateAccessKeyById 短信服务密钥修改失败, 查无此密钥: newAccessKey: {}", newAccessKey);
            return ResponseData.createFailResponseData("updateAccessKeyByIdInfo", false, "不存在此密钥", "key_is_not_exists");
        }
        newAccessKey.setUpdateTime(System.currentTimeMillis());
        accessKeyDao.updateAccessKey(newAccessKey);
        updateAccessKeyCatch(accessKeyMap.get(newAccessKey.getType()), newAccessKey, false);
        log.info("updateAccessKeyById 更新密钥成功 oldAccessKey: {}", oldAccessKey);
        log.info("updateAccessKeyById 更新密钥成功 newAccessKey: {}", newAccessKey);
        return ResponseData.createSuccessResponseData("updateAccessKeyByIdInfo", true);
    }

    /**
     * 删除短信服务密钥
     * @param id id
     * @return 返回是否删除成功
     */
    public ResponseData deleteAccessKeyById(Integer id) {
        log.info("deleteAccessKeyById 删除短信服务密钥 id: {}", id);
        AccessKey accessKey = accessKeyDao.selectAccessKey(id);
        if (accessKey == null) {
            log.info("deleteAccessKeyById 删除短信服务密钥失败, 密钥不存在");
            return ResponseData.createFailResponseData("deleteAccessKeyByIdInfo", false, "不存在此密钥", "key_is_not_exists");
        }
        accessKeyDao.deleteAccessKey(id);
        updateAccessKeyCatch(accessKeyMap.get(accessKey.getType()), accessKey, true);
        log.info("deleteAccessKeyById 删除短信服务密钥成功 accessKey: {}", accessKey);
        return ResponseData.createSuccessResponseData("deleteAccessKeyInfo", true);
    }

    /**
     * 更新缓存中的密钥
     * @param keyList 这个类型的密钥列表
     * @param accessKey 新的密钥
     * @param isDelete 是否删除
     */
    private void updateAccessKeyCatch(List<AccessKey> keyList, AccessKey accessKey, Boolean isDelete) {
        int targetKeyIndex = findTargetAccessKeyIndex(keyList, accessKey.getId());
        if (targetKeyIndex == -1) { // 当前本地缓存中不存在，更新进去
            log.info("updateAccessKeyCatch 更新本地短信服务密钥缓存，当前缓存中不存在，直接添加, accessKey: {}", accessKey);
            keyList.add(accessKey);
            return ;
        }
        if (isDelete) {
            log.info("updateAccessKeyCatch 更新本地短信服务密钥缓存，删除密钥缓存, accessKey: {}", accessKey);
            keyList.remove(targetKeyIndex);
        }
        else {
            log.info("updateAccessKeyCatch 更新本地短信服务密钥缓存，更新密钥缓存, accessKey: {}", accessKey);
            keyList.set(targetKeyIndex, accessKey);
        }
    }

    // 找到缓存中的密钥
    private int findTargetAccessKeyIndex(List<AccessKey> keyList, int targetId) {
        for (int i = 0; i < keyList.size(); i ++) {
            if (keyList.get(i).getId() == targetId) return i;
        }
        return -1;
    }



}
