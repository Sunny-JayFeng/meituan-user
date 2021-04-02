package jayfeng.com.meituan.loginregistry.constant;

/**
 * Cookie 常量
 * @author JayFeng
 * @date 2020/08/29
 */
public enum CookieConstant {

    USER_KEY("useruuid"),

//    DO_MAIN("mymeituan.com"),
    DO_MAIN("127.0.0.1"),
    PATH("/");

    private String message;

    CookieConstant(String message) {
        this.message = message;
    }

    public String getCookieKey() {
        return this.message;
    }

    public String getCookieDoMain() {
        return this.message;
    }

    public String getCookiePath() {
        return this.message;
    }

}
