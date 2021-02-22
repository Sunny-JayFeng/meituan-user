package jayfeng.com.meituan.loginregistry.util;

import org.springframework.stereotype.Component;

/**
 * 格式校验：手机号、邮箱、密码等等
 * @author JayFeng
 * @date 2020/11/25
 */
@Component
public class PatternMatch {

    /**
     * 校验手机号格式是否正确
     * @param phone
     * @return 返回是否正确
     */
    public Boolean isPhone(String phone) {
        return phone.matches("^1[0-9]{10}$");
    }

    /**
     * 邮箱格式是否正确
     * @param email 邮箱
     * @return 返回是否正确
     */
    public Boolean isEmail(String email) {
        return email.matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
    }

    /**
     * 密码格式是否符合要求
     * @param password 密码
     * @return 返回是否正确
     */
    public Boolean checkPassword(String password) {
        return password.length() >= 8 && password.length() <=32;
    }

}
