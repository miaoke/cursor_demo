package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.User;
import java.util.List;

@Mapper
public interface UserMapper {
    // 插入用户
    int insert(User user);
    
    // 根据ID查询用户
    User selectById(Long userId);
    
    // 查询所有用户
    List<User> selectAll();
    
    // 更新用户信息
    int update(User user);
    
    // 删除用户
    int delete(Long userId);
    
    // 根据用户名查询用户
    User selectByUsername(String username);
    
    // 根据用户名查询所有匹配的用户
    List<User> selectAllByUsername(String username);
    
    // 根据邮箱查询用户
    User selectByEmail(String email);
    
    // 根据邮箱查询所有匹配的用户
    List<User> selectAllByEmail(String email);
} 