package jayfeng.com.meituan.loginregistry.util;

import jayfeng.com.meituan.loginregistry.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author JayFeng
 * @date 2021/1/9
 */
@Component
public class TicketManagement {

    private Logger logger = LoggerFactory.getLogger(TicketManagement.class);

    @Autowired
    private RedisService redisService;

    /**
     * 创建一个 ticket，存入 redis 缓存中
     * @return 返回令牌
     */
    public String createTicket(String phone) {
        String ticket = UUID.randomUUID().toString();
        logger.info("createTicket, phone: {}, ticket: {}", phone, ticket);
        redisService.addTicket(phone, ticket);
        return ticket;
    }

    /**
     * 从 redis 缓存中获取一个 ticket
     * @param phone 手机号
     * @return 返回令牌
     */
    public String getTicket(String phone) {
        String ticket = redisService.getTicket(phone);
        logger.info("getTicket, phone: {}, ticket: {}", phone, ticket);
        return ticket;
    }

    /**
     * 判断号码在 redis 缓存中是否存在 ticket
     * @param phone 手机号
     * @return 返回 redis 中是否存在这个令牌
     */
    public Boolean ticketExists(String phone) {
        logger.info("ticketExists, phone: {}", phone);
        return redisService.ticketExists(phone);
    }

    /**
     * 从 redis 缓存中删除一个 ticket
     * @param phone 手机号
     */
    public void removeTicket(String phone) {
        logger.info("TicketManagement removeTicket, phone: {}", phone);
        Boolean result = redisService.removeTicket(phone);
        if (result == null || result) {
            logger.info("removeTicket, ticket删除成功");
        } else {
            logger.info("removeTicket, ticket删除失败");
        }
    }

}
