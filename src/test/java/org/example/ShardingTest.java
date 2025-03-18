package org.example;

import org.example.entity.User;
import org.example.entity.Order;
import org.example.service.UserService;
import org.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ShardingTest {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Test
    @Transactional
    public void testSharding() {
        // 1. 创建用户
        User user = new User();
        user.setUsername("sharding_test_user");
        user.setEmail("sharding_test@example.com");
        user.setCreatedAt(LocalDateTime.now());
        
        User savedUser = userService.createUser(user);
        assertNotNull(savedUser.getUserId(), "用户ID不应为空");
        
        // 2. 创建订单
        Order order = new Order();
        order.setUserId(savedUser.getUserId());
        order.setAmount(new BigDecimal("99.99"));
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        
        Order savedOrder = orderService.createOrder(order);
        assertNotNull(savedOrder, "订单不应为空");
        
        // 3. 查询用户订单
        List<Order> userOrders = orderService.getOrdersByUserId(savedUser.getUserId());
        assertFalse(userOrders.isEmpty(), "用户订单列表不应为空");
        assertEquals(1, userOrders.size(), "用户订单数量应为1");
        
        // 4. 验证订单信息
        Order fetchedOrder = userOrders.get(0);
        assertEquals(savedUser.getUserId(), fetchedOrder.getUserId(), "订单用户ID应匹配");
        assertEquals(new BigDecimal("99.99"), fetchedOrder.getAmount(), "订单金额应匹配");
        assertEquals("PENDING", fetchedOrder.getStatus(), "订单状态应匹配");
        
        System.out.println("分片测试完成！用户ID: " + savedUser.getUserId() + 
                         ", 订单ID: " + fetchedOrder.getOrderId());
    }
} 