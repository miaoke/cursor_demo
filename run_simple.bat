@echo off
chcp 65001
echo ========== 开始执行项目构建和运行 ==========

REM 编译和打包项目
echo 构建项目...
call mvn clean package -DskipTests

REM 使用simple配置运行应用
echo 启动项目...
java -jar target\cursor_demo-1.0-SNAPSHOT.jar --spring.profiles.active=simple

echo ========== 项目执行完成 ==========
pause 