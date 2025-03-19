package org.example.service;

import java.util.concurrent.TimeUnit;

/**
 * 缓存服务接口
 */
public interface CacheService {

    /**
     * 设置缓存
     *
     * @param key 键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 设置缓存，带过期时间
     *
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void set(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 获取缓存
     *
     * @param key 键
     * @param clazz 类型
     * @param <T> 泛型
     * @return 值
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 删除缓存
     *
     * @param key 键
     * @return 是否删除成功
     */
    boolean delete(String key);

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    boolean hasKey(String key);

    /**
     * 获取分布式锁
     *
     * @param lockKey 锁的key
     * @return 是否获取成功
     */
    boolean tryLock(String lockKey);

    /**
     * 获取分布式锁，带等待时间和过期时间
     *
     * @param lockKey 锁的key
     * @param waitTime 等待时间
     * @param leaseTime 锁过期时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit);

    /**
     * 释放分布式锁
     *
     * @param lockKey 锁的key
     */
    void unlock(String lockKey);
} 