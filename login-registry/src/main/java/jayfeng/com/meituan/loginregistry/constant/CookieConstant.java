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
    PATH("/"),
    MAX_AGE(3600 * 24); // cookie 在 24 小时后过期，后端有定时任务夜间清除redis中的session

    private String message;
    private Integer maxAge;

    CookieConstant(Integer maxAge) {
        this.maxAge = maxAge;
    }

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

    public Integer getCookieMaxAge() {
        return this.maxAge;
    }

}
