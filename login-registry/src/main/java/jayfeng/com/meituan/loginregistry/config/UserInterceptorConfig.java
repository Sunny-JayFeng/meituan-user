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
                .addPathPatterns("/meituan/user/**")
                .excludePathPatterns("/meituan/user/getIdentifyCode/*",
                                     "/meituan/user/loginByCode",
                                     "/meituan/user/loginByPassword",
                                     "/meituan/user/checkPhoneExist/*",
                                     "/meituan/user/checkIdCardExist/*",
                                     "/meituan/user/registry",
                                     "/meituan/user/checkAccountSafe",
                                     "/meituan/user/verifyCheckIdentifyCode",
                                     "/meituan/user/checkExistsTicket/*",
                                     "/meituan/user/retrievePassword",
                                     "/meituan/user/*.html",
                                     "/meituan/user/*.css",
                                     "/meituan/user/*.js");
    }

}
