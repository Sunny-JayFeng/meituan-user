package jayfeng.com.meituan.loginregistry.interceptor;

import jayfeng.com.meituan.loginregistry.constant.CookieConstant;
import jayfeng.com.meituan.loginregistry.constant.RedisConstant;
import jayfeng.com.meituan.loginregistry.handler.InterceptorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户请求拦截器
 * @author JayFeng
 * @date 2020/08/29
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
        boolean loginAllReady = preHandle(request, CookieConstant.USER_KEY.getCookieKey(), RedisConstant.USER_UUID_MAP.getRedisMapKey());
        System.out.println(request.getRequestURL());
        if (!loginAllReady) {
            log.info("用户未登录，重定向到登录页面");
            response.sendRedirect("/meituan/login/user/login.html");
        }
        return loginAllReady;
    }

}
