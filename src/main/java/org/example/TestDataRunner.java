package org.example;

import org.example.entity.Order;
import org.example.entity.User;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.example.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@Profile({"data-generator", "default"})
public class TestDataRunner implements CommandLineRunner {

    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
    
    private final Random random = new Random();
    private final String[] STATUS_LIST = {"PENDING", "PROCESSING", "COMPLETED", "CANCELLED"};

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("============ 正在执行测试数据生成 ============");
            // 创建admin用户
            createAdminUser();
            // 创建普通测试用户
            generateBatchData(10, 5);
            System.out.println("============ 测试数据生成完成 ============");
        } catch (Exception e) {
            System.err.println("数据生成过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 创建管理员用户
     */
    private void createAdminUser() {
        try {
            // 检查admin用户是否已存在
            User existingAdmin = userService.getUserByUsername("admin");
            if (existingAdmin == null) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@example.com");
                // 使用明确指定的管理员密码"admin123"
                adminUser.setPassword(PasswordUtil.encryptPassword("admin123"));
                adminUser.setCreatedAt(System.currentTimeMillis());
                
                adminUser = userService.createUser(adminUser);
                System.out.println("创建管理员用户成功: " + adminUser.getUsername() + ", ID: " + adminUser.getUserId());
            } else {
                System.out.println("管理员用户已存在，跳过创建");
            }
        } catch (Exception e) {
            System.err.println("创建管理员用户失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 生成批量测试数据
     */
    public void generateBatchData(int userCount, int ordersPerUser) {
        System.out.println("开始生成测试数据...");
        
        try {
            for (int i = 0; i < userCount; i++) {
                String username = "测试用户_" + i;
                
                // 检查用户是否已存在
                User existingUser = null;
                try {
                    existingUser = userService.getUserByUsername(username);
                } catch (Exception e) {
                    System.err.println("查询用户时出错: " + e.getMessage());
                    // 继续执行，创建新用户
                }
                
                User user;
                
                if (existingUser == null) {
                    // 创建新用户
                    user = createRandomUser(i);
                    user = userService.createUser(user);
                    System.out.println("创建用户: " + user.getUsername() + ", ID: " + user.getUserId());
                } else {
                    // 使用已存在的用户
                    user = existingUser;
                    System.out.println("用户已存在，跳过创建: " + user.getUsername() + ", ID: " + user.getUserId());
                }
                
                // 创建用户订单
                for (int j = 0; j < ordersPerUser; j++) {
                    try {
                        Order order = createRandomOrder(user.getUserId());
                        orderService.createOrder(order);
                        System.out.println("  创建订单: " + order.getOrderId() + ", 用户ID: " + user.getUserId());
                    } catch (Exception e) {
                        System.err.println("创建订单失败: " + e.getMessage());
                    }
                }
                
                if (i % 10 == 0 && i > 0) {
                    System.out.println("已处理 " + i + " 个用户");
                }
            }
        } catch (Exception e) {
            System.err.println("数据生成过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("测试数据生成完成！");
    }
    
    private User createRandomUser(int index) {
        User user = new User();
        user.setUsername("测试用户_" + index);
        user.setEmail("user" + index + "@example.com");
        user.setPassword(PasswordUtil.encryptPassword("password123"));
        user.setCreatedAt(System.currentTimeMillis());
        return user;
    }

    private Order createRandomOrder(Long userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setAmount(new BigDecimal(random.nextInt(10000) / 100.0).setScale(2, BigDecimal.ROUND_HALF_UP));
        order.setStatus(STATUS_LIST[random.nextInt(STATUS_LIST.length)]);
        order.setCreatedAt(System.currentTimeMillis());
        return order;
    }
} 