package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.constant.CacheConstants;
import org.example.entity.TestTable;
import org.example.mapper.TestTableMapper;
import org.example.service.CacheService;
import org.example.service.TestTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TestTableServiceImpl implements TestTableService {

    @Autowired
    private TestTableMapper testTableMapper;
    
    @Autowired
    private CacheService cacheService;
    
    private static final String TEST_CACHE_PREFIX = "test:";
    private static final String TEST_LOCK_PREFIX = "test_lock:";
    
    @Override
    @Transactional
    public TestTable create(TestTable testTable) {
        String lockKey = TEST_LOCK_PREFIX + "new";
        boolean locked = false;
        
        try {
            // 尝试获取分布式锁
            locked = cacheService.tryLock(lockKey, CacheConstants.LOCK_WAIT_TIME, CacheConstants.LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取创建测试数据分布式锁失败");
                throw new RuntimeException("创建测试数据失败，请稍后重试");
            }
            
            // 创建测试数据
            testTableMapper.insert(testTable);
            
            // 缓存测试数据
            if (testTable.getId() != null) {
                String cacheKey = TEST_CACHE_PREFIX + testTable.getId();
                cacheService.set(cacheKey, testTable, CacheConstants.CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
                log.info("测试数据创建并缓存成功 - id: {}", testTable.getId());
            }
            
            return testTable;
        } finally {
            // 释放分布式锁
            if (locked) {
                cacheService.unlock(lockKey);
            }
        }
    }

    @Override
    public TestTable getById(Integer id) {
        if (id == null) {
            return null;
        }
        
        String cacheKey = TEST_CACHE_PREFIX + id;
        
        // 尝试从缓存中获取
        TestTable testTable = cacheService.get(cacheKey, TestTable.class);
        if (testTable != null) {
            log.info("从缓存中获取测试数据 - id: {}", id);
            return testTable;
        }
        
        // 缓存未命中，从数据库获取
        testTable = testTableMapper.selectById(id);
        if (testTable != null) {
            // 存入缓存
            cacheService.set(cacheKey, testTable, CacheConstants.CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
            log.info("测试数据已缓存 - id: {}", id);
        }
        
        return testTable;
    }

    @Override
    public List<TestTable> getAll() {
        // 此处不使用缓存，直接从数据库获取所有数据
        // 如果数据量较小，可以考虑缓存
        return testTableMapper.selectAll();
    }

    @Override
    @Transactional
    public boolean update(TestTable testTable) {
        if (testTable.getId() == null) {
            return false;
        }
        
        String cacheKey = TEST_CACHE_PREFIX + testTable.getId();
        String lockKey = TEST_LOCK_PREFIX + testTable.getId();
        boolean locked = false;
        
        try {
            // 尝试获取分布式锁
            locked = cacheService.tryLock(lockKey, CacheConstants.LOCK_WAIT_TIME, CacheConstants.LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取更新测试数据分布式锁失败 - id: {}", testTable.getId());
                throw new RuntimeException("更新测试数据失败，请稍后重试");
            }
            
            // 更新测试数据
            boolean result = testTableMapper.update(testTable) > 0;
            
            if (result) {
                // 更新缓存
                if (cacheService.hasKey(cacheKey)) {
                    cacheService.set(cacheKey, testTable, CacheConstants.CACHE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
                    log.info("测试数据已更新并重新缓存 - id: {}", testTable.getId());
                } else {
                    cacheService.delete(cacheKey);
                    log.info("测试数据已更新，缓存已删除 - id: {}", testTable.getId());
                }
            }
            
            return result;
        } finally {
            // 释放分布式锁
            if (locked) {
                cacheService.unlock(lockKey);
            }
        }
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        if (id == null) {
            return false;
        }
        
        String cacheKey = TEST_CACHE_PREFIX + id;
        String lockKey = TEST_LOCK_PREFIX + id;
        boolean locked = false;
        
        try {
            // 尝试获取分布式锁
            locked = cacheService.tryLock(lockKey, CacheConstants.LOCK_WAIT_TIME, CacheConstants.LOCK_RELEASE_TIME, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取删除测试数据分布式锁失败 - id: {}", id);
                throw new RuntimeException("删除测试数据失败，请稍后重试");
            }
            
            // 删除测试数据
            boolean result = testTableMapper.delete(id) > 0;
            
            if (result) {
                // 直接删除缓存
                cacheService.delete(cacheKey);
                log.info("测试数据已删除，缓存已清除 - id: {}", id);
            }
            
            return result;
        } finally {
            // 释放分布式锁
            if (locked) {
                cacheService.unlock(lockKey);
            }
        }
    }
}