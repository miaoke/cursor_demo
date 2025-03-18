package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Order;
import java.util.List;

@Mapper
public interface OrderMapper {
    // 插入订单
    int insert(Order order);
    
    // 根据ID查询订单
    Order selectById(Integer orderId);
    
    // 查询所有订单
    List<Order> selectAll();
    
    // 更新订单信息
    int update(Order order);
    
    // 删除订单
    int delete(Integer orderId);
    
    // 根据用户ID查询订单列表
    List<Order> selectByUserId(Integer userId);
    
    // 根据订单状态查询订单列表
    List<Order> selectByStatus(String status);
} 