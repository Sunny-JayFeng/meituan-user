package jayfeng.com.meituan.loginregistry.constant;

/**
 * Redis 常量
 * @author JayFeng
 * @date 2020/08/29
 */
public enum RedisConstant {

    ADMIN_UUID_MAP("adminUUIDMap"),
    SELLER_UUID_MAP("sellerUUIDMap"),
    USER_UUID_MAP("userUUIDMap"),

    JSESSIONID_VALUE("VALUE"),

    IDENTIFY_TIMEOUT(60 * 10L), // 验证码的过期时间

    TICKET_KEY("ticket"),
    TICKET_TIMEOUT(60 * 5L), // ticket 的过期时间

    CHECK_SAFE_TIMES_SUFFIX("checkSafeTimes"),
    ACCOUNT_SAFE_TIMEOUT(24 * 3600L); // 次数超过时，限制 24 小时

    private String message;

    private Long timeout;

    RedisConstant(String message) {
        this.message = message;
    }
    RedisConstant(Long timeout) {
        this.timeout = timeout;
    }

    public String getRedisMapKey() {
        return this.message;
    }

    public String getValue() {
        return this.message;
    }

    public Long getTimeout() {
        return this.timeout;
    }

}
