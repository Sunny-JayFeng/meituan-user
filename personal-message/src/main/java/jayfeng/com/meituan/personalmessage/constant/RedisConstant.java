package jayfeng.com.meituan.personalmessage.constant;

/**
 * Redis 常量
 * @author JayFeng
 * @date 2020/08/29
 */
public enum RedisConstant {

    USER_UUID_MAP("userUUIDMap");

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
