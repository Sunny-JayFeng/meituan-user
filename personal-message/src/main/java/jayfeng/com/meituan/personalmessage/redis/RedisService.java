package jayfeng.com.meituan.personalmessage.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * redis业务逻辑
 * @author JayFeng
 * @date 2021/2/8
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

}
