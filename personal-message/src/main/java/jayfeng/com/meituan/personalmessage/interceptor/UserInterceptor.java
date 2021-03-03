package jayfeng.com.meituan.personalmessage.interceptor;

import jayfeng.com.meituan.personalmessage.constant.RedisConstant;
import jayfeng.com.meituan.personalmessage.handler.InterceptorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author JayFeng
 * @date 2021/3/3
 */
@Component
@Slf4j
public class UserInterceptor extends InterceptorHandler implements HandlerInterceptor {

    /**
     * 拦截请求，判断是否已登录
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return 返回 boolean 值，是否放行
     * @throws IOException
     */
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        boolean loginAllReady = preHandler(request, "useruuid", RedisConstant.USER_UUID_MAP.getRedisMapKey());
        if (!loginAllReady) {
            log.info("用户未登录，重定向到登录页面");
            response.sendRedirect("/meituan/login/user/login.html");
        }
        return loginAllReady;
    }

}
