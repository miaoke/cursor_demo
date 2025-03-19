package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面控制器
 */
@Controller
public class PageController {

    /**
     * 首页，直接跳转到登录页
     * @return 登录页面视图名
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/login.html";
    }

    /**
     * 登录页面
     * @return 登录页面视图名
     */
    @GetMapping("/login")
    public String login() {
        return "redirect:/login.html";
    }
    
    /**
     * 访问系统主页
     */
    @GetMapping("/home")
    public String home() {
        return "redirect:/index.html";
    }
    
    /**
     * 访问用户管理页面
     */
    @GetMapping("/user")
    public String user() {
        return "redirect:/user.html";
    }
    
    /**
     * 访问订单管理页面
     */
    @GetMapping("/order")
    public String order() {
        return "redirect:/order.html";
    }
    
    /**
     * 访问测试表管理页面
     */
    @GetMapping("/test")
    public String test() {
        return "redirect:/test.html";
    }
    
    /**
     * 访问系统信息页面
     */
    @GetMapping("/system-info")
    public String systemInfo() {
        return "redirect:/system-info.html";
    }
    
    @GetMapping("/redis-test")
    public String redisTest() {
        return "redirect:/redis_test.html";
    }
} 