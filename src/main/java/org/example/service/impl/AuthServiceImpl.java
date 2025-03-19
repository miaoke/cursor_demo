package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.entity.User;
import org.example.service.AuthService;
import org.example.service.UserService;
import org.example.util.JwtUtil;
import org.example.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("用户登录: {}", loginRequest.getUsername());
        
        // 查询用户
        User user = userService.getUserByUsername(loginRequest.getUsername());
        if (user == null) {
            log.warn("用户名不存在: {}", loginRequest.getUsername());
            return LoginResponse.builder()
                    .success(false)
                    .message("用户名或密码错误")
                    .build();
        }
        
        // 验证密码
        boolean isValid = PasswordUtil.validatePassword(loginRequest.getPassword(), user.getPassword());
        if (!isValid) {
            log.warn("密码错误: {}", loginRequest.getUsername());
            return LoginResponse.builder()
                    .success(false)
                    .message("用户名或密码错误")
                    .build();
        }
        
        // 生成令牌
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());
        
        // 更新最后登录时间
        user.setLastLoginTime(System.currentTimeMillis());
        userService.updateUser(user);
        
        // 返回登录成功响应
        return LoginResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .success(true)
                .message("登录成功")
                .build();
    }
    
    /**
     * 通过令牌验证用户
     * @param token JWT令牌
     * @return 登录响应
     */
    @Override
    public LoginResponse validateToken(String token) {
        // 验证令牌
        if (!jwtUtil.validateToken(token)) {
            return LoginResponse.builder()
                    .success(false)
                    .message("无效的令牌")
                    .build();
        }
        
        // 获取用户信息
        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        
        // 查询用户
        User user = userService.getUserById(userId);
        if (user == null) {
            return LoginResponse.builder()
                    .success(false)
                    .message("用户不存在")
                    .build();
        }
        
        // 验证用户名是否匹配
        if (!user.getUsername().equals(username)) {
            return LoginResponse.builder()
                    .success(false)
                    .message("令牌与用户不匹配")
                    .build();
        }
        
        // 返回验证成功响应
        return LoginResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .success(true)
                .message("令牌有效")
                .build();
    }
} 