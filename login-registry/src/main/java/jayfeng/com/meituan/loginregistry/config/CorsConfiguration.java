package jayfeng.com.meituan.loginregistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author JayFeng
 * @date 2020/9/10
 */
@Configuration
public class CorsConfiguration {

    /**
     * 开发环境配置允许跨域
     * todo 生产环境利用 Nginx 解决跨域
     * @return 返回 WebMvc 配置
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }

}
