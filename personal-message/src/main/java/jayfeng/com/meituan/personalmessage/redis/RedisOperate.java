package jayfeng.com.meituan.personalmessage.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis 操作
 * @author JayFeng
 * @date 2021/2/8
 */
@Component
public class RedisOperate {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 某个 map 中是否存在某个 key
     * @param hashKey 哪一个 map
     * @param key 哪一个 key
     * @return 返回是否存在
     */
    public Boolean isExistsKeyForHash(String hashKey, Object key) {
        return stringRedisTemplate.opsForHash().hasKey(hashKey, key);
    }

}
