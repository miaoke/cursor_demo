package org.example.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Redisson配置类
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.redisson.config}")
    private Resource configFile;

    /**
     * RedissonClient Bean
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        // 根据文件名后缀选择读取方式
        String configPath = configFile.getURI().toString();
        Config config;
        
        if (configPath.endsWith(".yml") || configPath.endsWith(".yaml")) {
            config = Config.fromYAML(configFile.getInputStream());
        } else if (configPath.endsWith(".json")) {
            config = Config.fromJSON(configFile.getInputStream());
        } else {
            // 手动从properties文件创建配置
            Properties props = new Properties();
            try (InputStream is = configFile.getInputStream()) {
                props.load(is);
            }
            
            config = new Config();
            
            // 配置单节点模式
            SingleServerConfig serverConfig = config.useSingleServer();
            String address = props.getProperty("singleServerConfig.address");
            if (address != null) {
                serverConfig.setAddress(address);
            }
            
            String password = props.getProperty("singleServerConfig.password");
            if (password != null && !password.equals("null")) {
                serverConfig.setPassword(password);
            }
            
            String database = props.getProperty("singleServerConfig.database");
            if (database != null) {
                serverConfig.setDatabase(Integer.parseInt(database));
            }
            
            String clientName = props.getProperty("singleServerConfig.clientName");
            if (clientName != null && !clientName.equals("null")) {
                serverConfig.setClientName(clientName);
            }
            
            String connectTimeout = props.getProperty("singleServerConfig.connectTimeout");
            if (connectTimeout != null) {
                serverConfig.setConnectTimeout(Integer.parseInt(connectTimeout));
            }
            
            String timeout = props.getProperty("singleServerConfig.timeout");
            if (timeout != null) {
                serverConfig.setTimeout(Integer.parseInt(timeout));
            }
            
            String retryAttempts = props.getProperty("singleServerConfig.retryAttempts");
            if (retryAttempts != null) {
                serverConfig.setRetryAttempts(Integer.parseInt(retryAttempts));
            }
            
            String retryInterval = props.getProperty("singleServerConfig.retryInterval");
            if (retryInterval != null) {
                serverConfig.setRetryInterval(Integer.parseInt(retryInterval));
            }
            
            String subscriptionConnectionMinimumIdleSize = props.getProperty("singleServerConfig.subscriptionConnectionMinimumIdleSize");
            if (subscriptionConnectionMinimumIdleSize != null) {
                serverConfig.setSubscriptionConnectionMinimumIdleSize(Integer.parseInt(subscriptionConnectionMinimumIdleSize));
            }
            
            String subscriptionConnectionPoolSize = props.getProperty("singleServerConfig.subscriptionConnectionPoolSize");
            if (subscriptionConnectionPoolSize != null) {
                serverConfig.setSubscriptionConnectionPoolSize(Integer.parseInt(subscriptionConnectionPoolSize));
            }
            
            String connectionMinimumIdleSize = props.getProperty("singleServerConfig.connectionMinimumIdleSize");
            if (connectionMinimumIdleSize != null) {
                serverConfig.setConnectionMinimumIdleSize(Integer.parseInt(connectionMinimumIdleSize));
            }
            
            String connectionPoolSize = props.getProperty("singleServerConfig.connectionPoolSize");
            if (connectionPoolSize != null) {
                serverConfig.setConnectionPoolSize(Integer.parseInt(connectionPoolSize));
            }
            
            String dnsMonitoringInterval = props.getProperty("singleServerConfig.dnsMonitoringInterval");
            if (dnsMonitoringInterval != null) {
                serverConfig.setDnsMonitoringInterval(Long.parseLong(dnsMonitoringInterval));
            }
            
            // 配置线程池
            String threads = props.getProperty("threads");
            if (threads != null) {
                config.setThreads(Integer.parseInt(threads));
            }
            
            String nettyThreads = props.getProperty("nettyThreads");
            if (nettyThreads != null) {
                config.setNettyThreads(Integer.parseInt(nettyThreads));
            }
            
            // 配置传输模式
            String transportMode = props.getProperty("transportMode");
            if (transportMode != null) {
                config.setTransportMode(org.redisson.config.TransportMode.valueOf(transportMode));
            }
        }
        
        return Redisson.create(config);
    }
} 