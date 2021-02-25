package jayfeng.com.meituan.personalmessage.controller;

import jayfeng.com.meituan.personalmessage.bean.Coupon;
import jayfeng.com.meituan.personalmessage.response.ResponseMessage;
import jayfeng.com.meituan.personalmessage.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户优惠券控制层
 * @author JayFeng
 * @date 2021/2/23
 */
@RestController
@RequestMapping("/meituan/user")
public class CouponController extends BaseController {

    Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;

    /**
     * 获取用户当前所有有效优惠券
     * @param userId 用户 id
     * @return 返回有效优惠券信息
     */
    @GetMapping("/getAllValidCoupons/{userId}")
    public ResponseMessage getAllValidCoupons(@PathVariable("userId") Integer userId) {
        logger.info("getAllValidCoupons 获取用户有效优惠券 userId: {}", userId);
        return requestSuccess(couponService.getAllValidCoupons(userId));
    }

    /**
     * 获取用户当前所有失效优惠券
     * @param userId 用户 id
     * @return 返回失效优惠券信息
     */
    @GetMapping("/getAllUnValidCoupons/{userId}")
    public ResponseMessage getAllUnValidCoupons(@PathVariable("userId") Integer userId) {
        logger.info("getAllUnValidCoupons 获取用户有效优惠券 userId: {}", userId);
        return requestSuccess(couponService.getAllUnValidCoupons(userId));
    }

    /**
     * 使用优惠券
     * @param couponId 优惠券id
     * @return 返回使用是否成功
     */
    @PutMapping("/useCoupon/{couponId}")
    public ResponseMessage useCoupon(@PathVariable("couponId") Integer couponId) {
        logger.info("useCoupon 使用优惠券 couponId: {}", couponId);
        return requestSuccess(couponService.useCoupon(couponId));
    }

    /**
     * 添加一张优惠券
     * @param coupon 优惠券信息
     * @return 返回添加是否成功
     */
    @PostMapping("/addCoupon")
    public ResponseMessage addCoupon(@RequestBody Coupon coupon) {
        logger.info("addCoupon 添加一张优惠券, coupon: {}", coupon);
        return requestSuccess(couponService.addCoupon(coupon));
    }

    /**
     * 删除一张失效的优惠券
     * @param couponId 优惠券 id
     * @return 返回删除是否成功
     */
    @PutMapping("/removeOneUnValidCoupon/{couponId}")
    public ResponseMessage removeOneUnValidCoupon(@PathVariable("couponId") Integer couponId) {
        logger.info("removeOneUnValidCoupon 删除一张失效优惠券 couponId: {}", couponId);
        return requestSuccess(couponService.removeOneUnValidCoupon(couponId));
    }

    /**
     * 清空失效优惠券
     * @param userId 用户 id
     * @return 返回清空是否成功
     */
    @PutMapping("/removeAllUnValidCoupons/{userId}")
    public ResponseMessage removeAllUnValidCoupons(@PathVariable("userId") Integer userId) {
        logger.info("removeAllUnValidCoupons 清空失效优惠券 userId: {}", userId);
        return requestSuccess(couponService.removeAllUnValidCoupons(userId));
    }
}
