package jayfeng.com.meituan.user.takeaway.controller;

import jayfeng.com.meituan.user.takeaway.bean.Order;
import jayfeng.com.meituan.user.takeaway.response.ResponseMessage;
import jayfeng.com.meituan.user.takeaway.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 外卖订单控制层
 * @author JayFeng
 * @date 2020/4/22
 */
@Slf4j
@RestController
@RequestMapping("/meituan/user/takeaway/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    /**
     * 根据用户 id 查询订单 分页查询
     * @param paramsMap 参数
     * @return 返回订单列表
     */
    @GetMapping("/findOrders")
    public ResponseMessage findOrders(@RequestParam Map<String, String> paramsMap) {
        log.info("findOrders 用户查询订单信息 paramsMap: {}", paramsMap);
        return requestSuccess(orderService.findOrders(paramsMap, getPage(paramsMap)));
    }

    /**
     * 根据订单 id 查询订单信息
     * @param orderId 订单 id
     * @return 返回订单信息
     */
    @GetMapping("/findOrderDetail/{orderId}")
    public ResponseMessage findOrderDetail(@PathVariable("orderId") Integer orderId) {
        log.info("findOrderDetail 根据订单 id 查询订单明细, orderId: {}", orderId);
        return requestSuccess(orderService.findOrderDetail(orderId));
    }

    /**
     * 根据订单 id 删除订单
     * @param orderId 订单 id
     * @return 返回
     */
    @DeleteMapping("/deleteOrder/{orderId}")
    public ResponseMessage deleteOrder(@PathVariable("orderId") Integer orderId) {
        log.info("deleteOrder 根据 id 删除订单 orderId: {}", orderId);
        return requestSuccess(orderService.deleteOrder(orderId));
    }

    /**
     * 支付
     * todo 看是否要使用第三方服务
     * @param paramsMap 参数
     * @return 返回
     */
    @PostMapping("/payServer")
    public ResponseMessage payServer(@RequestParam Map<String, String> paramsMap) {
        log.info("payServer 支付 paramsMap: {}", paramsMap);
        return requestSuccess(orderService.payServer(paramsMap));
    }

    /**
     * 下单
     * @param order 外卖订单
     * @return 返回
     */
    @PostMapping("/createOrder")
    public ResponseMessage createOrder(@RequestBody Order order) {
        log.info("createOrder 下单 order: {}", order);
        return requestSuccess(orderService.createOrder(order));
    }

    /**
     * 用户取消订单
     * @param orderId 订单 id
     * @return 返回
     */
    @PutMapping("/cancelOrder/{orderId}")
    public ResponseMessage cancelOrder(@PathVariable("orderId") Integer orderId) {
        log.info("cancelOrder 用户取消订单 orderId: {}", orderId);
        return requestSuccess(orderService.cancelOrder(orderId));
    }

}
