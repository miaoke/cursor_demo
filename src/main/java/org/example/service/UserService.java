package org.example.service;

import org.example.entity.User;
import java.util.List;

public interface UserService {
    // 创建用户
    User createUser(User user);
    
    // 根据ID获取用户
    User getUserById(Long userId);
    
    // 获取所有用户
    List<User> getAllUsers();
    
    // 更新用户信息
    User updateUser(User user);
    
    // 删除用户
    void deleteUser(Long userId);
    
    // 根据用户名获取用户
    User getUserByUsername(String username);
    
    // 根据邮箱获取用户
    User getUserByEmail(String email);
} 