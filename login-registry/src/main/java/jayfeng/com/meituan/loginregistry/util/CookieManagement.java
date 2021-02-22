package jayfeng.com.meituan.loginregistry.util;

import jayfeng.com.meituan.loginregistry.constant.CookieConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie 管理
 * @author JayFeng
 * @date 2020/08/29
 */
@Component
public class CookieManagement {

    private Logger logger = LoggerFactory.getLogger(CookieManagement.class);

    /**
     * 创建一个 cookie
     * @param key cookie 的 key
     * @param value cookie 的值
     * @return
     */
    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        if (key.equals(CookieConstant.ADMIN_KEY.getCookieKey())) {
            cookie.setMaxAge(-1); // 如果是管理员登录，则 cookie 在浏览器关闭后失效
        } else {
            cookie.setMaxAge(60 * 60 * 24 * 30 * 12 * 10); // 如果是商家或者用户登录，cookie 在 10 年后失效
        }
        cookie.setPath(CookieConstant.PATH.getCookiePath());
        cookie.setDomain(CookieConstant.DO_MAIN.getCookieDoMain());
        return cookie;
    }

    /**
     * 删除一个 cookie
     * @param cookies 所有 cookie
     * @param key cookie 的 key
     * @return
     */
    public String removeCookie(HttpServletResponse response, Cookie[] cookies, String key) {
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    logger.info("removeCookie, key: {}", key);
                    cookie.setMaxAge(0);
                    // 必须设置相同的 path 和 domain， setMaxAge 才有效
                    cookie.setPath(CookieConstant.PATH.getCookiePath());
                    cookie.setDomain(CookieConstant.DO_MAIN.getCookieDoMain());
                    response.addCookie(cookie);
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 给请求添加一个 cookie
     * @param response 响应，设置 cookie
     * @param key cookie 的 key
     * @param value cookie 的值
     */
    public void setCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = createCookie(key, value);
        setCookie(response, cookie);
    }

    /**
     * 给请求添加一个 cookie
     * @param response 响应，设置 cookie
     * @param cookie cookie
     */
    public void setCookie(HttpServletResponse response, Cookie cookie) {
        logger.info("setCookie, key: {}, value: {}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

}
