package jayfeng.com.meituan.uploadfile.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * 上传文件大小限制配置
 * @author JayFeng
 * @date 2021/2/6
 */
@Configuration
public class MultipartFileConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        DataSize oneFileDataSize = DataSize.ofMegabytes(5L); // 单个文件最大 5M
        factory.setMaxFileSize(oneFileDataSize);

        DataSize allFileDataSize = DataSize.ofMegabytes(50L); // 所有文件最大 50M
        factory.setMaxRequestSize(allFileDataSize);

        return factory.createMultipartConfig();
    }

}
