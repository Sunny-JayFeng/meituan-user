package jayfeng.com.meituan.personalmessage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"jayfeng.com.meituan.personalmessage.dao"})
public class PersonalMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalMessageApplication.class, args);
    }

}
