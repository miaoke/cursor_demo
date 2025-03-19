package org.example.constant;

/**
 * 缓存相关常量
 */
public class CacheConstants {
    /**
     * 缓存过期时间（秒）
     */
    public static final int CACHE_EXPIRATION_SECONDS = 5;

    /**
     * 用户缓存前缀
     */
    public static final String USER_CACHE_PREFIX = "user:";

    /**
     * 订单缓存前缀
     */
    public static final String ORDER_CACHE_PREFIX = "order:";

    /**
     * 用户订单缓存前缀
     */
    public static final String USER_ORDER_CACHE_PREFIX = "user_order:";
    
    /**
     * 用户锁前缀
     */
    public static final String USER_LOCK_PREFIX = "user_lock:";
    
    /**
     * 订单锁前缀
     */
    public static final String ORDER_LOCK_PREFIX = "order_lock:";
    
    /**
     * 锁等待时间（秒）
     */
    public static final int LOCK_WAIT_TIME = 3;
    
    /**
     * 锁释放时间（秒）
     */
    public static final int LOCK_RELEASE_TIME = 10;
} 