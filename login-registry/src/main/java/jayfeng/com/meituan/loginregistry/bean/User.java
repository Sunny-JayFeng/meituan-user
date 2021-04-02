package jayfeng.com.meituan.loginregistry.bean;

import lombok.Data;

/**
 * 用户
 * @author JayFeng
 * @date 2020/08/29
 */
@Data
public class User {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 头像
     * 存图片在服务器上的文件名
     */
    private String headImage;

    /**
     * 电话
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否为会员
     * 0 否
     * 1 是
     */
    private Integer isVIP;

    /**
     * 是否自动续费
     * 0 -- 否
     * 1 -- 是
     */
    private Integer automaticRenewal;

    /**
     * 会员开通时间
     */
    private Long vipCreateTime;

    /**
     * 会员到期时间
     * 根据开通时间跟会员类型自动计算
     */
    private Long vipEndTime;

    /**
     * 会员类型
     * 7天赠送
     * 月（按 30 天算）
     * 季（ 3 个月，3 * 30 天）
     * 年（ 365 天）
     * 如果用户不是会员，则这个值为 -1
     */
    private Integer vipType;

    /**
     * 会员等级
     */
    private Integer vipGrade;

    /**
     * 身份证 -- 实名认证
     */
    private String idCard;

    /**
     * 生日
     */
    private Long birthday;

    /**
     * 当前用户是否有效（用户选择注销，在 14 天内为无效，14 天之后删除用户）
     * 0 -- 无效
     * 1 -- 有效
     */
    private Integer isValid;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

}
