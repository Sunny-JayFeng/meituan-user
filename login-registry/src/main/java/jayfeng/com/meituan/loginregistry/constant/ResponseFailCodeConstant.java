package jayfeng.com.meituan.loginregistry.constant;

/**
 * 请求失败响应码
 * @author JayFeng
 * @date 2021/1/23
 */
public enum ResponseFailCodeConstant {

    KNOWN_EXCEPTION(500),

    SERVER_BUSY(503),

    UNKNOWN_EXCEPTION(999),

    FORBIDDEN(403);

    private Integer responseCode;

    ResponseFailCodeConstant(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getResponseCode() {
        return this.responseCode;
    }

}
