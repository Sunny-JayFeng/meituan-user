package jayfeng.com.meituan.loginregistry.handler;

import jayfeng.com.meituan.loginregistry.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 请求拦截逻辑
 * @author JayFeng
 * @date 2020/08/29
 */
@Component
public class InterceptorHandler {

    @Autowired
    private RedisService redisService;

    /**
     * 拦截请求，判断是否有 sessionId
     * 判断该 sessionId 是否存在 redis 中
     * @param request 请求
     * @param cookieKey cookie 的 key
     * @param redisMapKey redis 中的哪个 map
     * @return 返回是否已登录
     */
    public Boolean preHandle(HttpServletRequest request, String cookieKey, String redisMapKey) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                String key = cookie.getName();
                if (cookieKey.equals(key)) {
                    String value = cookie.getValue();
                    if (redisService.hasThisUUID(redisMapKey, value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
