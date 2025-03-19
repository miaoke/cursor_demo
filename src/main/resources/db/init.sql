-- 注意时间类型都为TIMESTAMP

-- 创建数据库
CREATE DATABASE IF NOT EXISTS cursor_demo;

-- 使用数据库
USE cursor_demo;

drop table users;
drop table users_0;
drop table users_1;
drop table users_2;
drop table users_3;
drop table users_4;

-- 创建原始用户表
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(32) NOT NULL COMMENT 'MD5加密密码',
    last_login_time TIMESTAMP NULL COMMENT '最后登录时间',
    created_at TIMESTAMP NULL,
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
);

-- 创建用户分片表-基于user_id
CREATE TABLE IF NOT EXISTS users_0 LIKE users;
CREATE TABLE IF NOT EXISTS users_1 LIKE users;
CREATE TABLE IF NOT EXISTS users_2 LIKE users;
CREATE TABLE IF NOT EXISTS users_3 LIKE users;
CREATE TABLE IF NOT EXISTS users_4 LIKE users;

-- 创建原始订单表（用于兼容）
CREATE TABLE IF NOT EXISTS orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    KEY idx_user_id (user_id),
    KEY idx_status (status)
);


-- 创建订单分片表-基于user_id取模
CREATE TABLE IF NOT EXISTS orders_0 LIKE orders;
CREATE TABLE IF NOT EXISTS orders_1 LIKE orders;
CREATE TABLE IF NOT EXISTS orders_2 LIKE orders;
CREATE TABLE IF NOT EXISTS orders_3 LIKE orders;
CREATE TABLE IF NOT EXISTS orders_4 LIKE orders;

-- 创建测试表
CREATE TABLE IF NOT EXISTS test_table (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加初始管理员账户（密码: admin123）- 密码为MD5加密
INSERT INTO users (username, email, password, created_at) VALUES 
('admin', 'admin@example.com', 'e7ef17a5c3a3bd46a7e4fa62c7edf288', NOW()); 