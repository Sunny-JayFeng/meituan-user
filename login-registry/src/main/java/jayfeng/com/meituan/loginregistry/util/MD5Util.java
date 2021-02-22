package jayfeng.com.meituan.loginregistry.util;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * 密码加密工具类
 * @author JayFeng
 * @date 2021/1/26
 */
@Component
public class MD5Util {

    private static final String SALT = "PWD_SALT";

    /**
     * 不带盐，纯 MD5 加密
     * @param password 密码
     * @return 返回加密后的密码，纯 MD5 加密
     */
    private String encryptWithoutSalt(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    /**
     * 加盐加密
     * @param password 密码
     * @return 返回加盐加密后的密码
     */
    private String encryptWithSalt(String password) {
        password = password + SALT;
        return encrypt(password);
    }

    /**
     * 纯 MD5 加密和加盐加密的结果，各取一半。
     * 这样保证不可破解
     * @param password 密码
     * @return 返回加密后的密码
     */
    public String encrypt(String password) {
        String withoutSalt = encryptWithoutSalt(password);
        String withSalt = encryptWithSalt(password);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 32; i ++) { // 加密后是 32 位的
            if (i % 2 == 0) {
                result.append(withoutSalt.charAt(i));
            } else {
                result.append(withSalt.charAt(i));
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword("SALT");
        System.out.println(encryptor.encrypt("12345"));
        System.out.println(encryptor.encrypt("12345"));
        System.out.println(encryptor.decrypt("rfTue/XwXtDhiA1aHH+VpA=="));
        System.out.println(encryptor.decrypt("RAef6wc4oIDqbGA7PFbRpw=="));
    }

}
