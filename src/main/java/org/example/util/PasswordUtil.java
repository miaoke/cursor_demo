package org.example.util;

import org.example.constant.SecurityConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 密码加密工具类
 */
public class PasswordUtil {

    /**
     * 生成随机盐值
     * @param length 盐值长度
     * @return 随机盐值字符串
     */
    public static String generateSalt(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            salt.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        return salt.toString();
    }
    
    /**
     * 使用MD5加密密码
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        return encryptPassword(password, SecurityConstants.DEFAULT_SALT);
    }
    
    /**
     * 使用MD5加密密码
     * @param password 原始密码
     * @param salt 盐值
     * @return 加密后的密码
     */
    public static String encryptPassword(String password, String salt) {
        try {
            // 将密码和盐值拼接
            String passwordWithSalt = password + salt;
            
            // 创建MD5摘要算法实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // 加密
            byte[] hash = md.digest(passwordWithSalt.getBytes());
            
            // 将字节数组转为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密失败", e);
        }
    }
    
    /**
     * 验证密码
     * @param inputPassword 输入的原始密码
     * @param encryptedPassword 数据库中存储的加密密码
     * @return 密码是否正确
     */
    public static boolean validatePassword(String inputPassword, String encryptedPassword) {
        String encryptedInput = encryptPassword(inputPassword);
        return encryptedInput.equals(encryptedPassword);
    }
} 