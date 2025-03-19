package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.service.CacheService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务实现类
 */
@Slf4j
@Service
public class RedisCacheServiceImpl implements CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    private static final long DEFAULT_LOCK_LEASE_TIME = 30L; // 默认锁过期时间30秒

    @Override
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            log.info("Redis缓存设置成功 - key: {}", key);
        } catch (Exception e) {
            log.error("Redis缓存设置失败 - key: {}, error: {}", key, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            log.info("Redis缓存设置成功(带过期时间) - key: {}, timeout: {}, unit: {}", key, timeout, unit);
        } catch (Exception e) {
            log.error("Redis缓存设置失败(带过期时间) - key: {}, timeout: {}, unit: {}, error: {}", 
                key, timeout, unit, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            log.info("Redis缓存获取 - key: {}, value: {}", key, value);
            return value == null ? null : (T) value;
        } catch (Exception e) {
            log.error("Redis缓存获取失败 - key: {}, error: {}", key, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean delete(String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            log.info("Redis缓存删除 - key: {}, result: {}", key, result);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Redis缓存删除失败 - key: {}, error: {}", key, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean hasKey(String key) {
        try {
            Boolean result = redisTemplate.hasKey(key);
            log.info("Redis缓存检查key是否存在 - key: {}, result: {}", key, result);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Redis缓存检查key是否存在失败 - key: {}, error: {}", key, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean tryLock(String lockKey) {
        return tryLock(lockKey, 0, DEFAULT_LOCK_LEASE_TIME, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            boolean locked = lock.tryLock(waitTime, leaseTime, unit);
            log.info("Redisson分布式锁获取 - lockKey: {}, waitTime: {}, leaseTime: {}, unit: {}, result: {}", 
                lockKey, waitTime, leaseTime, unit, locked);
            return locked;
        } catch (Exception e) {
            log.error("Redisson分布式锁获取失败 - lockKey: {}, waitTime: {}, leaseTime: {}, unit: {}, error: {}", 
                lockKey, waitTime, leaseTime, unit, e.getMessage(), e);
            throw new RuntimeException("获取分布式锁失败", e);
        }
    }

    @Override
    public void unlock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("Redisson分布式锁释放 - lockKey: {}", lockKey);
            }
        } catch (Exception e) {
            log.error("Redisson分布式锁释放失败 - lockKey: {}, error: {}", lockKey, e.getMessage(), e);
            throw new RuntimeException("释放分布式锁失败", e);
        }
    }
} 