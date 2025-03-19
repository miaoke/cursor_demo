package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.constant.CacheConstants;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.CacheService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    @Transactional
    public User createUser(User user) {
        // 使用用户名作为锁的key，防止并发创建相同用户名的用户
        String lockKey = CacheConstants.USER_LOCK_PREFIX + (user.getUsername() != null ? user.getUsername() : "new");
        boolean locked = false;
        
        try {
            // 尝试获取分布式锁
            locked = cacheService.tryLock(lockKey, CacheConstants.LOCK_WAIT_TIME, CacheConstants.LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取创建用户分布式锁失败 - username: {}", user.getUsername());
                throw new RuntimeException("创建用户失败，请稍后重试");
            }
            
            // 创建用户
            userMapper.insert(user);
            
            // 缓存用户信息
            String cacheKey = CacheConstants.USER_CACHE_PREFIX + user.getUserId();
            cacheService.set(cacheKey, user, CacheConstants.CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
            log.info("用户创建并缓存成功 - userId: {}, username: {}", user.getUserId(), user.getUsername());
            
            return user;
        } finally {
            // 释放分布式锁
            if (locked) {
                cacheService.unlock(lockKey);
            }
        }
    }

    @Override
    public User getUserById(Long userId) {
        String cacheKey = CacheConstants.USER_CACHE_PREFIX + userId;
        
        // 尝试从缓存中获取
        User user = cacheService.get(cacheKey, User.class);
        if (user != null) {
            log.info("从缓存中获取用户信息 - userId: {}", userId);
            return user;
        }
        
        // 缓存未命中，从数据库获取
        user = userMapper.selectById(userId);
        if (user != null) {
            // 存入缓存
            cacheService.set(cacheKey, user, CacheConstants.CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
            log.info("用户信息已缓存 - userId: {}", userId);
        }
        
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        // 直接从数据库获取所有用户
        return userMapper.selectAll();
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        String cacheKey = CacheConstants.USER_CACHE_PREFIX + user.getUserId();
        String lockKey = CacheConstants.USER_LOCK_PREFIX + user.getUserId();
        boolean locked = false;
        
        try {
            // 尝试获取分布式锁
            locked = cacheService.tryLock(lockKey, CacheConstants.LOCK_WAIT_TIME, CacheConstants.LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取更新用户分布式锁失败 - userId: {}", user.getUserId());
                throw new RuntimeException("更新用户失败，请稍后重试");
            }
            
            // 更新用户信息
            userMapper.update(user);
            
            // 先删除缓存再重新设置，避免缓存不一致
            cacheService.delete(cacheKey);
            // 重新缓存用户信息
            cacheService.set(cacheKey, user, CacheConstants.CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
            log.info("用户信息已更新并重新缓存 - userId: {}", user.getUserId());
            
            return user;
        } finally {
            // 释放分布式锁
            if (locked) {
                cacheService.unlock(lockKey);
            }
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        String cacheKey = CacheConstants.USER_CACHE_PREFIX + userId;
        String lockKey = CacheConstants.USER_LOCK_PREFIX + userId;
        boolean locked = false;
        
        try {
            // 尝试获取分布式锁
            locked = cacheService.tryLock(lockKey, CacheConstants.LOCK_WAIT_TIME, CacheConstants.LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取删除用户分布式锁失败 - userId: {}", userId);
                throw new RuntimeException("删除用户失败，请稍后重试");
            }
            
            // 删除用户
            userMapper.delete(userId);
            
            // 直接删除缓存
            cacheService.delete(cacheKey);
            log.info("用户已删除，缓存已清除 - userId: {}", userId);
        } finally {
            // 释放分布式锁
            if (locked) {
                cacheService.unlock(lockKey);
            }
        }
    }

    @Override
    public User getUserByUsername(String username) {
        // 直接从数据库获取，不使用缓存
        // 如需缓存，可以考虑用username为key建立索引缓存
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        // 直接从数据库获取，不使用缓存
        // 如需缓存，可以考虑用email为key建立索引缓存
        return userMapper.selectByEmail(email);
    }
} 