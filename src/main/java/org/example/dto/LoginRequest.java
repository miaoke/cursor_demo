package org.example.dto;

import lombok.Data;

/**
 * 登录请求DTO
 */
@Data
public class LoginRequest {
    /**
     * 用户名或邮箱
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 是否记住登录
     */
    private Boolean rememberMe;
} 