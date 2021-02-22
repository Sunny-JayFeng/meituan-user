package jayfeng.com.meituan.personalmessage.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * redis业务逻辑
 * @author JayFeng
 * @date 2021/2/8
 */
@Service
public class RedisService {

    private Logger logger = LoggerFactory.getLogger(RedisService.class);

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
        logger.info("hasThisUUID redis缓存中是否存在这个uuid, redisKey: {}, sessionId: {}, result: {}", redisKey, sessionId, result);
        return result;
    }

}
