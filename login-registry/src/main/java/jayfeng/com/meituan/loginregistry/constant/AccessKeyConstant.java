package jayfeng.com.meituan.loginregistry.constant;

/**
 * 短信密钥常量
 * @author JayFeng
 * @date 2021/3/27
 */
public enum AccessKeyConstant {

    // 短信验证码
    SHORT_MESSAGE_CODE(0);

    AccessKeyConstant(Integer accessKeyType) {
        this.accessKeyType = accessKeyType;
    }

    private Integer accessKeyType;

    public Integer getAccessKeyType() {
        return this.accessKeyType;
    }

}
