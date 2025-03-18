package org.example.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

public class ConnectionTest {
    public static void main(String[] args) {
        try {
            // 加载MyBatis配置文件
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            // 获取SqlSession
            try (SqlSession session = sqlSessionFactory.openSession()) {
                // 获取数据库连接
                Connection connection = session.getConnection();
                
                // 测试连接
                if (connection != null && !connection.isClosed()) {
                    System.out.println("数据库连接成功！");
                    
                    // 执行简单查询测试
                    try {
                        connection.createStatement().executeQuery("SELECT 1");
                        System.out.println("数据库查询测试成功！");
                    } catch (Exception e) {
                        System.out.println("数据库查询测试失败：" + e.getMessage());
                    }
                } else {
                    System.out.println("数据库连接失败！");
                }
            }
        } catch (IOException e) {
            System.out.println("加载配置文件失败：" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("连接测试发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
} 