package org.example.constant;

/**
 * 安全相关常量
 */
public class SecurityConstants {
    /**
     * 默认盐值
     */
    public static final String DEFAULT_SALT = "admin123salt";
    
    /**
     * JWT密钥
     */
    public static final String JWT_SECRET = "cmkJwtSecretKey";
    
    /**
     * JWT过期时间（毫秒），默认24小时
     */
    public static final long JWT_EXPIRATION = 86400000;
    
    /**
     * JWT记住我过期时间（毫秒），默认7天
     */
    public static final long JWT_REMEMBER_ME_EXPIRATION = 604800000;
} 