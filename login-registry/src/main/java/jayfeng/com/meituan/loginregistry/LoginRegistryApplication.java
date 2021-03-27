package jayfeng.com.meituan.loginregistry;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"jayfeng.com.meituan.loginregistry.dao"})
public class LoginRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginRegistryApplication.class, args);
    }

}
