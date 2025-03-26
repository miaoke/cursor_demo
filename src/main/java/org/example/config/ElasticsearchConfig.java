package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.time.Duration;

@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = "org.example.repository.es")
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${spring.elasticsearch.rest.uris}")
    private String elasticsearchUri;

    @Value("${spring.elasticsearch.rest.connection-timeout}")
    private String connectionTimeoutStr;

    @Value("${spring.elasticsearch.rest.read-timeout}")
    private String readTimeoutStr;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        log.info("正在配置Elasticsearch客户端，URI: {}", elasticsearchUri);
        try {
            // 转换连接超时和读取超时为Duration
            Duration connectTimeout = parseDuration(connectionTimeoutStr, "5s");
            Duration readTimeout = parseDuration(readTimeoutStr, "30s");
            
            final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                    .connectedTo(elasticsearchUri.replace("http://", ""))
                    .withConnectTimeout(connectTimeout)
                    .withSocketTimeout(readTimeout)
                    .build();
            
            return RestClients.create(clientConfiguration).rest();
        } catch (Exception e) {
            log.error("配置Elasticsearch客户端时发生错误", e);
            throw e;
        }
    }
    
    @Bean
    public ElasticsearchOperations elasticsearchOperations() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
    
    /**
     * 将字符串格式的持续时间转换为Duration对象
     * 
     * @param durationStr 字符串格式的持续时间，例如 "5s", "30s"
     * @param defaultValue 默认值，如果解析失败则使用此值
     * @return Duration对象
     */
    private Duration parseDuration(String durationStr, String defaultValue) {
        try {
            if (durationStr == null || durationStr.isEmpty()) {
                return Duration.parse("PT" + defaultValue.toUpperCase().replace("S", "S"));
            }
            return Duration.parse("PT" + durationStr.toUpperCase().replace("S", "S"));
        } catch (Exception e) {
            log.warn("解析持续时间 '{}' 失败，使用默认值 '{}'", durationStr, defaultValue);
            return Duration.parse("PT" + defaultValue.toUpperCase().replace("S", "S"));
        }
    }
} 