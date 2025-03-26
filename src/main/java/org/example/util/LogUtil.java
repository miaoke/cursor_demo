package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class LogUtil {
    
    /**
     * 获取指定类的日志记录器
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
    
    /**
     * 记录信息日志
     */
    public static void info(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }
    
    /**
     * 记录错误日志
     */
    public static void error(Class<?> clazz, String message) {
        getLogger(clazz).error(message);
    }
    
    /**
     * 记录错误日志（带异常）
     */
    public static void error(Class<?> clazz, String message, Throwable e) {
        getLogger(clazz).error(message, e);
    }
    
    /**
     * 记录调试日志
     */
    public static void debug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }
    
    /**
     * 记录警告日志
     */
    public static void warn(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }
    
    /**
     * 记录跟踪日志
     */
    public static void trace(Class<?> clazz, String message) {
        getLogger(clazz).trace(message);
    }
} 