package org.example.service;

import org.example.entity.Order;
import java.util.List;

public interface OrderService {
    // 创建订单
    Order createOrder(Order order);
    
    // 根据ID获取订单
    Order getOrderById(Long orderId);
    
    // 获取所有订单
    List<Order> getAllOrders();
    
    // 更新订单信息
    Order updateOrder(Order order);
    
    // 删除订单
    void deleteOrder(Long orderId);
    
    // 根据用户ID获取订单列表
    List<Order> getOrdersByUserId(Long userId);
    
    // 根据订单状态获取订单列表
    List<Order> getOrdersByStatus(String status);
} 