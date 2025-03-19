@echo off
chcp 65001
echo ========== 重新创建表结构 ==========

mysql -u root -p123456 cursor_demo < src\main\resources\db\init_tables.sql

echo ========== 数据库重置完成 ==========
pause 