package org.example.util;

import org.example.entity.User;
import org.example.entity.Order;
import org.example.service.UserService;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    private final Random random = new Random();
    private final String[] STATUS_ARRAY = {"PENDING", "PROCESSING", "COMPLETED", "CANCELLED"};

    @Override
    public void run(String... args) {
        initializeData();
    }

    public void initializeData() {
        // 生成1000个用户
        for (int i = 1; i <= 1000; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@example.com");
            user.setCreatedAt(LocalDateTime.now());
            
            try {
                User savedUser = userService.createUser(user);
                
                // 为每个用户生成1-3个订单
                int orderCount = random.nextInt(3) + 1;
                for (int j = 0; j < orderCount; j++) {
                    Order order = new Order();
                    order.setUserId(savedUser.getUserId());
                    order.setAmount(new BigDecimal(random.nextInt(10000) / 100.0));
                    order.setStatus(STATUS_ARRAY[random.nextInt(STATUS_ARRAY.length)]);
                    order.setCreatedAt(LocalDateTime.now());
                    
                    orderService.createOrder(order);
                }
            } catch (Exception e) {
                System.err.println("Error creating user " + i + ": " + e.getMessage());
            }
        }
        
        System.out.println("数据初始化完成！");
    }
} 