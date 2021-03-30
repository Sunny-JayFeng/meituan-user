package jayfeng.com.meituan.loginregistry.config;

import jayfeng.com.meituan.loginregistry.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 用户请求拦截器配置类
 * @author JayFeng
 * @date 2020/08/29
 */
@Configuration
public class UserInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private UserInterceptor userInterceptor;

    /**
     * 配置拦截路径和放通路径
     * @param registry 注册器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/meituan/user/login_registry/**")
                .excludePathPatterns("/meituan/user/login_registry/getIdentifyCode/*",
                                     "/meituan/user/login_registry/loginByCode",
                                     "/meituan/user/login_registry/loginByPassword",
                                     "/meituan/user/login_registry/checkPhoneExist/*",
                                     "/meituan/user/login_registry/checkIdCardExist/*",
                                     "/meituan/user/login_registry/registry",
                                     "/meituan/user/login_registry/checkAccountSafe",
                                     "/meituan/user/login_registry/verifyCheckIdentifyCode",
                                     "/meituan/user/login_registry/checkExistsTicket/*",
                                     "/meituan/user/login_registry/retrievePassword",
                                     "/meituan/user/login_registry/*.html",
                                     "/meituan/user/login_registry/*.css",
                                     "/meituan/user/login_registry/*.js");
    }

}
