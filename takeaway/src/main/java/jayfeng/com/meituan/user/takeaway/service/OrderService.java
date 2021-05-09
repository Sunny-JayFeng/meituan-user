package jayfeng.com.meituan.user.takeaway.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jayfeng.com.meituan.user.takeaway.bean.Order;
import jayfeng.com.meituan.user.takeaway.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 外卖订单业务层
 * @author JayFeng
 * @date 2020/4/22
 */
@Service
public interface OrderService {

    /**
     * 根据用户 id 查询订单 分页查询
     * @param paramsMap 参数
     * @return 返回订单列表
     */
    ResponseData findOrders(Map<String, String> paramsMap, Page<Order> page);

    /**
     * 根据订单 id 查询订单信息
     * @param orderId 订单 id
     * @return 返回订单信息
     */
    ResponseData findOrderDetail(Integer orderId);

    /**
     * 根据订单 id 删除订单
     * @param orderId 订单 id
     * @return 返回
     */
    ResponseData deleteOrder(Integer orderId);

    /**
     * 支付
     * @param paramsMap 参数
     * @return 返回
     */
    ResponseData payServer(Map<String, String> paramsMap);

    /**
     * 下单
     * @param order 外卖订单
     * @return 返回
     */
    ResponseData createOrder(Order order);

    /**
     * 用户取消订单
     * @param orderId 订单 id
     * @return 返回
     */
    ResponseData cancelOrder(Integer orderId);

}
