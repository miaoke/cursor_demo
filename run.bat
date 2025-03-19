@echo off
chcp 65001
echo ========== Starting project build and run ==========

REM Display Java version
echo Checking Java environment...
java -version

REM Compile and package the project
echo Building the project...
call mvn clean package -DskipTests

REM Initialize database (optional)
echo Initializing database...
REM mysql -u root -p123456 < src\main\resources\db\init.sql

REM Run Spring Boot application
echo Starting the application...
call java -jar target\cursor_demo-1.0-SNAPSHOT.jar --spring.profiles.active=data-generator

echo ========== Project execution completed ========= 