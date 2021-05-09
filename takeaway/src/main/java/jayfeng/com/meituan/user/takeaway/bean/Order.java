package jayfeng.com.meituan.user.takeaway.bean;

import lombok.Data;

/**
 * 外卖订单实体
 * @author JayFeng
 * @date 2020/4/22
 */
@Data
public class Order {

    /**
     * 订单 id
     */
    private Integer id;

    /**
     * 下单用户 id
     */
    private Integer userId;

    /**
     * 订单状态
     * -1 -- 订单已取消
     * 0 -- 商家未接单 -- 此时可以修改备注
     * 1 -- 商家已接单骑手未接单 -- 不可修改信息，无法联系骑手
     * 2 -- 骑手已接单 -- 可联系骑手
     * 3 -- 订单已完成
     */
    private Integer status;

    /**
     * 商店 id
     */
    private Integer storeId;

    /**
     * 商店图片 为了展示所有订单的时候不用再去远程调用获取商店信息
     */
    private String storeImage;

    /**
     * 商店名称 为了展示所有订单的时候不用再去远程调用获取商店信息
     */
    private String storeName;

    /**
     * 购买的商品的名称和数量
     */
    private String goodsMessage;

    /**
     * 商品 id 英文逗号分隔
     * 1,2,3,4,5
     */
    private String goodIds;

    /**
     * 打包费用
     */
    private Float packFees;

    /**
     * 配送费
     */
    private Float shippingFees;

    /**
     * 准时宝费用
     * 如果值是 -1 证明是商家赠送，前端展示的时候展示为 0，并且备注上（商家赠送）
     */
    private Float onTimeFees;

    /**
     * 津贴优惠
     */
    private Float allowanceDiscount;

    /**
     * 美团红包
     */
    private Float luckyMoney;

    /**
     * 满减优惠
     */
    private Float fullDiscount;

    /**
     * 优惠券
     */
    private Float coupon;

    /**
     * 优惠券 id
     */
    private Integer couponId;

    /**
     * 总费用
     */
    private Float totalFees;

    /**
     * 已优惠
     */
    private Float discountFees;

    /**
     * 期望送达时间
     */
    private String expectedTime;

    /**
     * 配送地址
     */
    private String deliveryAddress;

    /**
     * 配送服务
     * 美团配送，还是什么配送
     */
    private String deliveryServer;

    /**
     * 配送员 id
     */
    private Integer courierId;

    /**
     * 服务保障 id
     * todo 需要去了解是什么东西
     */
    private Integer serviceGuaranteeId;

    /**
     * 订单号码
     */
    private String orderNumber;

    /**
     * 支付方式
     * 0 -- 在线支付
     */
    private Integer payType;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 评价 id
     */
    private Integer evaluationId;

    /**
     * 创建时间 -- 下单时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

}
