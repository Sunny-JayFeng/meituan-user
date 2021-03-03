package jayfeng.com.meituan.loginregistry.util;

import jayfeng.com.meituan.loginregistry.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author JayFeng
 * @date 2021/1/9
 */
@Component
@Slf4j
public class TicketManagement {

    @Autowired
    private RedisService redisService;

    /**
     * 创建一个 ticket，存入 redis 缓存中
     * @return 返回令牌
     */
    public String createTicket(String phone) {
        String ticket = UUID.randomUUID().toString();
        log.info("createTicket, phone: {}, ticket: {}", phone, ticket);
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
        log.info("getTicket, phone: {}, ticket: {}", phone, ticket);
        return ticket;
    }

    /**
     * 判断号码在 redis 缓存中是否存在 ticket
     * @param phone 手机号
     * @return 返回 redis 中是否存在这个令牌
     */
    public Boolean ticketExists(String phone) {
        log.info("ticketExists, phone: {}", phone);
        return redisService.ticketExists(phone);
    }

    /**
     * 从 redis 缓存中删除一个 ticket
     * @param phone 手机号
     */
    public void removeTicket(String phone) {
        log.info("TicketManagement removeTicket, phone: {}", phone);
        Boolean result = redisService.removeTicket(phone);
        if (result == null || result) {
            log.info("removeTicket, ticket删除成功");
        } else {
            log.info("removeTicket, ticket删除失败");
        }
    }

}
