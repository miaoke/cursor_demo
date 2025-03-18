-- 创建数据库
CREATE DATABASE IF NOT EXISTS cursor_demo;

-- 使用数据库
USE cursor_demo;

-- 创建原始用户表（用于兼容）
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL,
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
);

-- 创建用户分片表
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
    created_at DATETIME NOT NULL,
    KEY idx_user_id (user_id),
    KEY idx_status (status)
);

-- 创建订单分片表
CREATE TABLE IF NOT EXISTS orders_0 LIKE orders;
CREATE TABLE IF NOT EXISTS orders_1 LIKE orders;
CREATE TABLE IF NOT EXISTS orders_2 LIKE orders;
CREATE TABLE IF NOT EXISTS orders_3 LIKE orders;
CREATE TABLE IF NOT EXISTS orders_4 LIKE orders; 