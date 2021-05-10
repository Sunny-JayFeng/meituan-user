package jayfeng.com.meituan.user.takeaway.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jayfeng.com.meituan.user.takeaway.bean.Order;
import jayfeng.com.meituan.user.takeaway.dao.OrderDao;
import jayfeng.com.meituan.user.takeaway.exception.RequestForbiddenException;
import jayfeng.com.meituan.user.takeaway.response.ResponseData;
import jayfeng.com.meituan.user.takeaway.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 外卖订单业务层实现类
 * @author JayFeng
 * @date 2020/4/22
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    /**
     * 根据用户 id 查询订单 分页查询
     * @param paramsMap 参数
     * @return 返回订单列表
     */
    @Override
    public ResponseData findOrders(Map<String, String> paramsMap, Page<Order> page) {
        return null;
    }

    /**
     * 根据订单 id 查询订单信息
     * todo 商品信息(远程调用, id 和 单价)、配送员信息(远程调用)
     * 组合之后返回
     * @param orderId 订单 id
     * @return 返回订单信息
     */
    @Override
    public ResponseData findOrderDetail(Integer orderId) {
        Order order = orderDao.selectOrderById(orderId);
        if (order == null) throw new RequestForbiddenException("您无权访问该服务");
        log.info("findOrderDetail 根据订单 id 查询订单信息 order: {}", order);
        Integer storeId = order.getStoreId();
        String goodIds = order.getGoodIds();
        Integer courierId = order.getCourierId();
        Map<String, Object> resultData = new HashMap<>(4);
        resultData.put("order", order);
        resultData.put("goodsMessageList", "远程调用获取商品信息");
        resultData.put("courierMessage", "远程调用获取配送员信息");
        return ResponseData.createSuccessResponseData("findOrderDetailInfo", resultData);
    }

    /**
     * 根据订单 id 删除订单
     * @param orderId 订单 id
     * @return 返回
     */
    @Override
    public ResponseData deleteOrder(Integer orderId) {
        Order order = orderDao.selectOrderById(orderId);
        if (order == null) throw new RequestForbiddenException("您无权访问该服务");
        log.info("deleteOrder 删除订单信息 order: {}", order);
        orderDao.deleteOrderById(orderId);
        return ResponseData.createSuccessResponseData("deleteOrderInfo", true);
    }

    /**
     * 支付
     * todo 远程调用 数据返回
     * 0 -- 成功
     * 1 -- 失败 余额不足
     * 2 -- 失败 (未知原因：繁忙)
     * @param paramsMap 参数
     * @return 返回
     */
    @Override
    public ResponseData payServer(Map<String, String> paramsMap) {
        int status = new Random().nextInt(3);
        if (status == 0) {
            log.info("payServer 支付成功");
            return ResponseData.createSuccessResponseData("payServerInfo", true);
        } else if (status == 1) {
            log.info("payServer 支付失败 余额不足");
            return ResponseData.createFailResponseData("payServerInfo", false, "余额不足", "insufficient_balance");
        } else {
            log.info("payServer 支付失败 未知错误 status: {}", status);
            return ResponseData.createFailResponseData("payServerInfo", false, "系统繁忙, 请稍后重试", "server_busy");
        }
    }

    /**
     * 下单 -- 订单创建的初始状态 -- 商家未接单
     * 创建订单、扣除津贴、扣除美团红包(若有)、更新优惠券信息(已使用)
     * todo 创建订单服务(远程调用)
     * 若都执行成功, 创建订单, 更新进数据库
     * @param order 外卖订单
     * @return 返回
     */
    @Override
    public ResponseData createOrder(Order order) {
        return null;
    }

    /**
     * 用户取消订单
     * todo -- 远程调用 订单用户可取消, 商家可接单, 骑手可接单和确认送达, 都是同一个接口, 改订单的状态
     * todo -- 如果商家未接单, 可以直接取消, 如果商家已接单, 需要提交申请, 消息通知商家审核
     * todo -- 取消订单, 需要远程调用退款
     * @param orderId 订单 id
     * @return 返回
     */
    @Override
    public ResponseData cancelOrder(Integer orderId) {
        return null;
    }

}
