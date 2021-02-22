package jayfeng.com.meituan.personalmessage.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收货地址
 * @author JayFeng
 * @date 2021/2/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddress {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户 id
     */
    private Integer userId;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 门牌号
     */
    private String doorCode;

    /**
     * 联系人
     */
    private String name;

    /**
     * 性别
     * 0 -- 先生
     * 1 -- 女士
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 标签
     * 0 -- 家
     * 1 -- 公司
     * 2 -- 学校
     */
    private Integer tag;

    /**
     * 是否为默认地址
     * 0 -- 否
     * 1 -- 是
     */
    private Integer isDefault;

    /**
     * 修改时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;


}
