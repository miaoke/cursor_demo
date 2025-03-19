package org.example.controller;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisTestController {
    private static final Logger logger = LoggerFactory.getLogger(RedisTestController.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/test")
    public String testRedis() {
        logger.info("开始测试Redis连接...");
        
        // 测试Lettuce
        String key = "test:lettuce";
        String value = "Hello Redis with Lettuce!" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(key, value, 10, TimeUnit.MINUTES);
        String result = redisTemplate.opsForValue().get(key);
        logger.info("Lettuce读取结果: {}", result);
        
        // 测试Redisson
        RBucket<String> bucket = redissonClient.getBucket("test:redisson");
        bucket.set("Hello Redis with Redisson!" + System.currentTimeMillis(), 10, TimeUnit.MINUTES);
        String redissonResult = bucket.get();
        logger.info("Redisson读取结果: {}", redissonResult);
        
        return "Redis测试成功! Lettuce: " + result + ", Redisson: " + redissonResult;
    }
    
    @GetMapping("/lock")
    public String testRedissonLock() {
        logger.info("开始测试Redisson分布式锁...");
        String lockKey = "test:lock";
        RLock lock = redissonClient.getLock(lockKey);
        
        try {
            // 尝试获取锁，最多等待3秒，锁持有时间为10秒
            boolean isLocked = lock.tryLock(3, 10, TimeUnit.SECONDS);
            logger.info("获取锁状态: {}", isLocked);
            
            if (isLocked) {
                logger.info("成功获取到锁，执行业务逻辑");
                // 模拟业务处理
                Thread.sleep(1000);
                return "成功获取到锁并执行业务逻辑";
            } else {
                logger.info("未能获取到锁");
                return "未能获取到锁";
            }
        } catch (Exception e) {
            logger.error("锁操作异常", e);
            return "锁操作异常: " + e.getMessage();
        } finally {
            // 如果当前线程持有锁，则释放锁
            if (lock.isHeldByCurrentThread()) {
                logger.info("释放锁");
                lock.unlock();
            }
        }
    }
    
    @GetMapping("/set/{key}/{value}")
    public String setValue(@PathVariable String key, @PathVariable String value) {
        logger.info("设置键值对: key={}, value={}", key, value);
        redisTemplate.opsForValue().set("test:" + key, value);
        return "设置成功";
    }
    
    @GetMapping("/get/{key}")
    public String getValue(@PathVariable String key) {
        logger.info("获取键值: key={}", key);
        String value = redisTemplate.opsForValue().get("test:" + key);
        logger.info("获取结果: {}", value);
        return value != null ? value : "键不存在";
    }
    
    @GetMapping("/delete/{key}")
    public String deleteValue(@PathVariable String key) {
        logger.info("删除键: key={}", key);
        Boolean result = redisTemplate.delete("test:" + key);
        logger.info("删除结果: {}", result);
        return result ? "删除成功" : "键不存在";
    }
} 