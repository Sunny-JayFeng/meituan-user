package jayfeng.com.meituan.loginregistry.bean;

import lombok.Data;

/**
 * 验证码密钥
 * @author JayFeng
 * @date 2021/3/27
 */
@Data
public class AccessKey {

    private Integer id;

    private String regionId;

    private String accessKeyId;

    private String secret;

    private Integer type;

    private Long createTime;

    private Long updateTime;

}
