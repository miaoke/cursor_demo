package org.example.service;

import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;

/**
 * 认证服务接口
 */
public interface AuthService {
    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * 通过令牌验证用户
     * @param token JWT令牌
     * @return 登录响应
     */
    LoginResponse validateToken(String token);
} 