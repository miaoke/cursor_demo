package org.example;

import org.example.entity.Order;
import org.example.entity.User;
import org.example.mapper.OrderMapper;
import org.example.mapper.UserMapper;
import org.example.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
@Profile("simple-data")
public class SimpleTestDataRunner implements CommandLineRunner {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private OrderMapper orderMapper;
    
    private final Random random = new Random();
    private final String[] STATUSES = {"PENDING", "PROCESSING", "COMPLETED", "CANCELLED"};

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(PasswordUtil.encryptPassword("password123"));
        user.setCreatedAt(System.currentTimeMillis());
        userMapper.insert(user);
        
        System.out.println("Created test user: " + user.getUserId());
        
        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setAmount(BigDecimal.valueOf(random.nextDouble() * 100).setScale(2, BigDecimal.ROUND_HALF_UP));
        order.setStatus(STATUSES[random.nextInt(STATUSES.length)]);
        order.setCreatedAt(System.currentTimeMillis());
        orderMapper.insert(order);
        
        System.out.println("Created test order: " + order.getOrderId() + " for user: " + user.getUserId());
    }
} 