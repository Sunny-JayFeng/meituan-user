package jayfeng.com.meituan.personalmessage.config;

import jayfeng.com.meituan.personalmessage.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 用户请求拦截器
 * @author JayFeng
 * @date 2021/3/3
 */
@Configuration
public class UserInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private UserInterceptor userInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/meituan/user/**");
    }
}
