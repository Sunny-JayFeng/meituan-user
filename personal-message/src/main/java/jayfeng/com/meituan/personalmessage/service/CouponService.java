package jayfeng.com.meituan.personalmessage.service;

import jayfeng.com.meituan.personalmessage.bean.Coupon;
import jayfeng.com.meituan.personalmessage.dao.CouponDao;
import jayfeng.com.meituan.personalmessage.dao.UserDao;
import jayfeng.com.meituan.personalmessage.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 用户优惠券业务逻辑
 * @author JayFeng
 * @date 2021/2/23
 */
@Service
@Slf4j
public class CouponService {

    private static final Integer VALID_STATUS = 1; // 优惠券有效状态
    private static final Integer UN_VALID_STATUS = 0; // 优惠券失效状态

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private UserDao userDao;

    /**
     * 获取用户当前所有有效优惠券
     * @param userId 用户 id
     * @return 返回有效优惠券信息
     */
    public ResponseData getAllValidCoupons(Integer userId) {
        List<Coupon> couponList = couponDao.findAllCouponByUserId(userId, VALID_STATUS);
        log.info("getAllValidCoupons 获取用户有效优惠券成功 userId: {}, size: {}", userId, couponList.size());
        return ResponseData.createSuccessResponseData("getAllValidCoupons", couponList);
    }

    /**
     * 获取用户当前所有失效优惠券
     * @param userId 用户 id
     * @return 返回失效优惠券信息
     */
    public ResponseData getAllUnValidCoupons(Integer userId) {
        List<Coupon> couponList = couponDao.findAllCouponByUserId(userId, UN_VALID_STATUS);
        log.info("getAllUnValidCoupons 获取用户失效优惠券成功 userId: {}, size: {}", userId, couponList.size());
        return ResponseData.createSuccessResponseData("getAllUnValidCouponsInfo", couponList);
    }

    /**
     * 使用优惠券
     * @param couponId 优惠券 id
     * @return 返回使用是否成功
     */
    public ResponseData useCoupon(Integer couponId) {
        Coupon coupon = couponDao.findCouponById(couponId);
        if (coupon == null || coupon.getStatus().equals(UN_VALID_STATUS)) {
            log.info("useCoupon 优惠券已失效, 使用失败 coupon: {}", coupon);
            return ResponseData.createFailResponseData("useCouponInfo", false, "优惠券已失效", "coupon_is_un_valid");
        }
        couponDao.updateCouponStatus(couponId, UN_VALID_STATUS);
        log.info("useCoupon 优惠券使用成功 coupon: {}", coupon);
        return ResponseData.createSuccessResponseData("useCouponInfo", true);
    }

    /**
     * 优惠券过了使用期限，设置失效
     * @param couponList 优惠券列表
     */
    public void batchSetCouponUnValid(List<Coupon> couponList) {
        log.info("batchSetCouponUnValid 批量设置优惠券失效, size: {}", couponList.size());
        for (Coupon coupon : couponList) {
            log.info("batchSetCouponUnValid 优惠券失效, coupon: {}", coupon);
            couponDao.updateCouponStatus(coupon.getId(), UN_VALID_STATUS);
        }
    }

    /**
     * 添加一张优惠券
     * @param coupon 优惠券信息
     * @return 返回添加是否成功
     */
    public ResponseData addCoupon(Coupon coupon) {
        if (coupon == null) {
            log.info("addCoupon 优惠券为空, 添加失败");
            return ResponseData.createFailResponseData("addCouponInfo", false, "优惠券不存在", "coupon_is_not_exists");
        }
        Integer userId = coupon.getUserId();
        if (ObjectUtils.isEmpty(userId) || userDao.selectIdById(userId) == null) {
            log.info("addCoupon 用户不存在, 优惠券添加失败 userId: {}", userId);
            return ResponseData.createFailResponseData("addCouponInfo", false, "用户不存在", "user_is_not_exists");
        }
        coupon.setStatus(VALID_STATUS);
        coupon.setCreateTime(System.currentTimeMillis());
        coupon.setUpdateTime(coupon.getCreateTime());
        couponDao.insertCoupon(coupon);
        log.info("addCoupon 优惠券添加成功 coupon: {}", coupon);
        return ResponseData.createSuccessResponseData("addCouponInfo", true);
    }

    /**
     * 删除一张失效的优惠券
     * @param couponId 优惠券id
     * @return 返回删除是否成功
     */
    public ResponseData removeOneUnValidCoupon(Integer couponId) {
        Coupon coupon = couponDao.findCouponById(couponId);
        log.info("removeOneUnValidCoupon 删除一张失效优惠券, coupon: {}", coupon);
        couponDao.deleteOneUnValidCoupon(couponId, UN_VALID_STATUS);
        return ResponseData.createSuccessResponseData("removeOneUnValidCouponInfo", true);
    }

    /**
     * 清空失效优惠券
     * @param userId 用户 id
     * @return 返回清空是否成功
     */
    public ResponseData removeAllUnValidCoupons(Integer userId) {
        List<Coupon> couponList = couponDao.findAllCouponByUserId(userId, UN_VALID_STATUS);
        log.info("removeAllUnValidCoupons 清空失效优惠券, size: {}", couponList.size());
        couponDao.deleteAllUnValidCoupons(userId, UN_VALID_STATUS);
        return ResponseData.createSuccessResponseData("removeAllUnValidCouponsInfo", true);
    }
}
