package jayfeng.com.meituan.loginregistry.redis;

import jayfeng.com.meituan.loginregistry.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * redis 操作
 * @author JayFeng
 * @date 2020/08/29
 */
@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisOperate redisOperate;

    /**
     * 是否存在这个 uuid
     * @param redisKey 哪一个 map
     * @param sessionId map 中是否存在这个 key
     * @return 返回是否存在这个 uuid
     */
    public Boolean hasThisUUID(String redisKey, String sessionId) {
        Boolean result = redisOperate.isExistsKeyForHash(redisKey, sessionId);
        log.info("hasThisUUID redis缓存中是否存在这个uuid, redisKey: {}, sessionId: {}, result: {}", redisKey, sessionId, result);
        return result;
    }

    /**
     * 获取某个 map 中的某个键值对的值
     * @param redisKey map key
     * @param sessionId 键
     * @return 值
     */
    public Object getUserJSON(String redisKey, String sessionId) {
        Object userObj = redisOperate.getValueForHash(redisKey, sessionId);
        log.info("getUserJSON 从 redis 缓存中获取用户 userObj: {}", userObj);
        return userObj;
    }

    /**
     * 存入 uuid
     * @param redisKey 哪一个 map
     * @param sessionId 存入的 key
     */
    public void addUUID(String redisKey, String sessionId, String objectStr) {
        log.info("addUUID 向redis缓存中添加一个uuid, redisKey: {}, UUID: {}", redisKey, sessionId);
        redisOperate.setForHash(redisKey, sessionId, objectStr);
    }

    /**
     * 退出登录
     * 删除 uuid
     * @param redisKey 哪一个 map
     * @param sessionId 删除的 key
     */
    public void deleteUUID(String redisKey, String sessionId) {
        log.info("deleteUUID 从redis缓存中删除一个uuid, redisKey: {}, UUID: {}", redisKey, sessionId);
        redisOperate.removeForHash(redisKey, sessionId);
    }

    /**
     * 获取短信验证码
     * @param phone key-手机号
     * @return 返回验证码的值
     */
    public String getIdentifyCode(String phone) {
        log.info("getIdentifyCode 从redis缓存中获取短信验证码, phone: {}", phone);
        return redisOperate.get(phone);
    }

    /**
     * 向 redis 缓存中添加一个验证码，有效时间为 10 分钟
     * @param phone key-手机号
     * @param identifyCode 验证码
     */
    public void addIdentifyCode(String phone, String identifyCode) {
        log.info("addIdentifyCode 向redis缓存中添加一个验证码, phone: {}, identifyCode: {}", phone, identifyCode);
        redisOperate.set(phone, identifyCode, RedisConstant.IDENTIFY_TIMEOUT.getTimeout(), TimeUnit.SECONDS);
    }

    /**
     * 从 redis 缓存中移除一个验证码
     * @param phone key-手机号
     */
    public Boolean removeIdentifyCode(String phone) {
        log.info("removeIdentifyCode, 从redis缓存中移除一个验证码, phone: {}", phone);
        return redisOperate.remove(phone);
    }

    /**
     * 获取手机验证码的过期时间
     * @param phone 手机号
     * @return 返回过期时间
     */
    public Long getTimeout(String phone) {
        log.info("getTimeout 获取手机验证码的过期时间, phone: {}", phone);
        return redisOperate.getTimeout(phone, TimeUnit.SECONDS);
    }

    /**
     * 向 redis 缓存中添加一个 ticket，有效时间为 5 分钟
     * @param phone key-手机号
     * @param ticket 令牌值
     */
    public void addTicket(String phone, String ticket) {
        log.info("addTicket 向redis缓存中添加一个令牌, phone: {}, ticket: {}", phone, ticket);
        redisOperate.set(phone + RedisConstant.TICKET_KEY.getValue(), ticket, RedisConstant.TICKET_TIMEOUT.getTimeout(), TimeUnit.SECONDS);
    }

    /**
     * 从 redis 缓存中获取一个 ticket
     * @param phone key-手机号
     * @return 返回令牌的值
     */
    public String getTicket(String phone) {
        log.info("getTicket 从redis缓存中获取一个令牌, phone: {}", phone);
        return redisOperate.get(phone + RedisConstant.TICKET_KEY.getValue());
    }

    /**
     * 判断号码在 redis 缓存中是否存在 ticket
     * @param phone key-手机号
     * @return 返回是否存在令牌
     */
    public Boolean ticketExists(String phone) {
        Boolean result = redisOperate.isExistsKey(phone + RedisConstant.TICKET_KEY.getValue());
        log.info("ticketExists 判断这个号码在redis缓存中是否存在令牌 phone: {}, result: {}", phone, result);
        return result;
    }

    /**
     * 从 redis 缓存中移除一个 ticket
     * @param phone key-手机号
     * @return 返回删除是否成功
     */
    public Boolean removeTicket(String phone) {
        log.info("removeTicket 从redis缓存中删除这个手机号的令牌 phone: {}", phone);
        return redisOperate.remove(phone + RedisConstant.TICKET_KEY.getValue());
    }

    /**
     * 向 redis 缓存中设置该账号已进行了多少次安全性校验
     * 过期时间为 24 小时
     * @param userId 用户 id
     * @param times 次数
     */
    public void setCheckSafeTimes(String userId, Integer times) {
        log.info("setCheckSafeTimes 设置该账号已进行了多少次安全性校验, userId: {}, times: {}", userId, times);
        redisOperate.set(userId + RedisConstant.CHECK_SAFE_TIMES_SUFFIX.getValue(), times.toString(), RedisConstant.ACCOUNT_SAFE_TIMEOUT.getTimeout(), TimeUnit.SECONDS);
    }

    /**
     * 从 redis 缓存中获取该账号已进行了多少次安全性校验
     * @param userId 用户 id
     * @return 返回次数
     */
    public Integer getCheckSafeTimes(String userId) {
        log.info("getCheckSafeTimes 获取该账号已进行了多少次安全性校验, userId: {}", userId);
        String times = redisOperate.get(userId + RedisConstant.CHECK_SAFE_TIMES_SUFFIX.getValue());
        if (times == null) return 0;
        return Integer.parseInt(times);
    }

}
