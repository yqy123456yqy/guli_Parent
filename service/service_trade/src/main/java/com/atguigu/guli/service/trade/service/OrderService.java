package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-05-09
 */
public interface OrderService extends IService<Order> {

    String getOrder(String courseId, String memberId);

    Order getByOrderId(String orderId, String memberId);

    boolean selectIsBuy(String courseId, String memberId);

    List<Order> selectOrder(String memberId);

    boolean removeById(String orderId, String memberId);

    Order getOrderByOrderNo(String orderNo);


    void updateOrderStutas(Map<String, String> map);

    boolean queryPayStatus(String orderNo);

}
