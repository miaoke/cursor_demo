package org.example.service.impl;

import org.example.entity.Order;
import org.example.mapper.OrderMapper;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public Order createOrder(Order order) {
        orderMapper.insert(order);
        return order;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        return orderMapper.selectById(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.selectAll();
    }

    @Override
    @Transactional
    public boolean updateOrder(Order order) {
        return orderMapper.update(order) > 0;
    }

    @Override
    @Transactional
    public boolean deleteOrder(Integer orderId) {
        return orderMapper.delete(orderId) > 0;
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderMapper.selectByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderMapper.selectByStatus(status);
    }
} 