package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.constant.CacheConstants;
import org.example.entity.Order;
import org.example.mapper.OrderMapper;
import org.example.service.CacheService;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private CacheService cacheService;
    
    @Override
    @Transactional
    public Order createOrder(Order order) {
        // 使用用户ID作为锁的key，防止并发创建订单
        String lockKey = CacheConstants.USER_LOCK_PREFIX + order.getUserId();
        boolean locked = false;
        
        try {
            // 尝试获取分布式锁
            locked = cacheService.tryLock(lockKey, CacheConstants.LOCK_WAIT_TIME, CacheConstants.LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取创建订单分布式锁失败 - userId: {}", order.getUserId());
                throw new RuntimeException("创建订单失败，请稍后重试");
            }
            
            // 创建订单
            orderMapper.insert(order);
            
            // 缓存订单信息
            String cacheKey = CacheConstants.ORDER_CACHE_PREFIX + order.getOrderId();
            cacheService.set(cacheKey, order, CacheConstants.CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
            
            // 删除用户订单列表缓存，强制下次查询重新加载
            String userOrdersCacheKey = CacheConstants.USER_ORDER_CACHE_PREFIX + order.getUserId();
            cacheService.delete(userOrdersCacheKey);
            
            log.info("订单创建并缓存成功 - orderId: {}, userId: {}", order.getOrderId(), order.getUserId());
            
            return order;
        } finally {
            // 释放分布式锁
            if (locked) {
                cacheService.unlock(lockKey);
            }
        }
    }

    @Override
    public Order getOrderById(Long orderId) {
        String cacheKey = CacheConstants.ORDER_CACHE_PREFIX + orderId;
        
        // 尝试从缓存中获取
        Order order = cacheService.get(cacheKey, Order.class);
        if (order != null) {
            log.info("从缓存中获取订单信息 - orderId: {}", orderId);
            return order;
        }
        
        // 缓存未命中，从数据库获取
        order = orderMapper.selectById(orderId);
        if (order != null) {
            // 存入缓存
            cacheService.set(cacheKey, order, CacheConstants.CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
            log.info("订单信息已缓存 - orderId: {}", orderId);
        }
        
        return order;
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        String cacheKey = CacheConstants.USER_ORDER_CACHE_PREFIX + userId;
        
        // 尝试从缓存中获取用户订单列表
        @SuppressWarnings("unchecked")
        List<Order> orders = cacheService.get(cacheKey, List.class);
        if (orders != null) {
            log.info("从缓存中获取用户订单列表 - userId: {}", userId);
            return orders;
        }
        
        // 缓存未命中，从数据库获取
        orders = orderMapper.selectByUserId(userId);
        if (orders != null && !orders.isEmpty()) {
            // 存入缓存
            cacheService.set(cacheKey, orders, CacheConstants.CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
            log.info("用户订单列表已缓存 - userId: {}, orderCount: {}", userId, orders.size());
        }
        
        return orders;
    }

    @Override
    public List<Order> getAllOrders() {
        // 直接从数据库获取所有订单，不使用缓存
        return orderMapper.selectAll();
    }

    @Override
    @Transactional
    public Order updateOrder(Order order) {
        String cacheKey = CacheConstants.ORDER_CACHE_PREFIX + order.getOrderId();
        String userOrdersCacheKey = CacheConstants.USER_ORDER_CACHE_PREFIX + order.getUserId();
        String lockKey = CacheConstants.ORDER_LOCK_PREFIX + order.getOrderId();
        boolean locked = false;
        
        try {
            // 尝试获取分布式锁
            locked = cacheService.tryLock(lockKey, CacheConstants.LOCK_WAIT_TIME, CacheConstants.LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取更新订单分布式锁失败 - orderId: {}", order.getOrderId());
                throw new RuntimeException("更新订单失败，请稍后重试");
            }
            
            // 更新订单信息
            orderMapper.update(order);
            
            // 删除然后重新设置订单缓存
            cacheService.delete(cacheKey);
            cacheService.set(cacheKey, order, CacheConstants.CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
            log.info("订单信息已更新并重新缓存 - orderId: {}", order.getOrderId());
            
            // 删除用户订单列表缓存，强制下次查询重新加载
            cacheService.delete(userOrdersCacheKey);
            log.info("用户订单列表缓存已删除 - userId: {}", order.getUserId());
            
            return order;
        } finally {
            // 释放分布式锁
            if (locked) {
                cacheService.unlock(lockKey);
            }
        }
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        // 先查询订单，获取用户ID
        Order order = getOrderById(orderId);
        if (order == null) {
            log.warn("订单不存在，无法删除 - orderId: {}", orderId);
            return;
        }
        
        String cacheKey = CacheConstants.ORDER_CACHE_PREFIX + orderId;
        String userOrdersCacheKey = CacheConstants.USER_ORDER_CACHE_PREFIX + order.getUserId();
        String lockKey = CacheConstants.ORDER_LOCK_PREFIX + orderId;
        boolean locked = false;
        
        try {
            // 尝试获取分布式锁
            locked = cacheService.tryLock(lockKey, CacheConstants.LOCK_WAIT_TIME, CacheConstants.LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取删除订单分布式锁失败 - orderId: {}", orderId);
                throw new RuntimeException("删除订单失败，请稍后重试");
            }
            
            // 删除订单
            orderMapper.delete(orderId);
            
            // 删除订单缓存
            cacheService.delete(cacheKey);
            
            // 删除用户订单列表缓存
            cacheService.delete(userOrdersCacheKey);
            
            log.info("订单已删除，缓存已清除 - orderId: {}, userId: {}", orderId, order.getUserId());
            
        } finally {
            // 释放分布式锁
            if (locked) {
                cacheService.unlock(lockKey);
            }
        }
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderMapper.selectByStatus(status);
    }
} 