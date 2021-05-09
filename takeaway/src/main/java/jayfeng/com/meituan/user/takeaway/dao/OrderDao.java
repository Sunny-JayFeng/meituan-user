package jayfeng.com.meituan.user.takeaway.dao;

import jayfeng.com.meituan.user.takeaway.bean.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 外卖订单持久层
 * @author JayFeng
 * @date 2020/4/22
 */
@Repository
public interface OrderDao {

    /**
     * 根据订单 id 查询订单信息
     * @param orderId 订单 id
     * @return 返回订单信息
     */
    @Select("SELECT * FROM `order` WHERE `id` = #{orderId}")
    Order selectOrderById(@Param("orderId") Integer orderId);

    /**
     * 根据订单 id 删除订单
     * @param orderId 订单 id
     * @return 返回
     */
    @Delete("DELETE FROM `order` WHERE `id` = #{orderId}")
    void deleteOrderById(@Param("orderId") Integer orderId);

}
