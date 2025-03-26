package org.example.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.example.entity.es.ProductDocument;
import org.example.service.es.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ElasticsearchDataInitializer {

    private final RestHighLevelClient elasticsearchClient;
    private final ProductService productService;

    @Bean
    public CommandLineRunner initElasticsearchData() {
        return args -> {
            try {
                // 检查索引是否存在
                boolean indexExists = elasticsearchClient.indices().exists(
                        new GetIndexRequest("products"), RequestOptions.DEFAULT);

                // 如果索引不存在，则创建索引
                if (!indexExists) {
                    log.info("创建Elasticsearch索引: products");
                    createProductIndex();
                }

                log.info("开始初始化Elasticsearch测试数据...");
                
                // 确保索引中没有数据
                productService.deleteAll();
                log.info("已清空现有数据");
                
                // 创建测试数据
                List<ProductDocument> products = createTestProducts();
                
                // 保存测试数据
                for (ProductDocument product : products) {
                    productService.save(product);
                    log.info("已保存产品: {}", product.getName());
                }
                
                log.info("Elasticsearch测试数据初始化完成，共{}条", products.size());
            } catch (Exception e) {
                log.error("初始化Elasticsearch数据时发生错误", e);
            }
        };
    }
    
    private void createProductIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("products");
        
        // 设置索引配置
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
                .build());
        
        // 设置映射
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                    .startObject("properties")
                        .startObject("id")
                            .field("type", "long")
                        .endObject()
                        .startObject("name")
                            .field("type", "text")
                            .field("analyzer", "standard")
                        .endObject()
                        .startObject("description")
                            .field("type", "text")
                            .field("analyzer", "standard")
                        .endObject()
                        .startObject("price")
                            .field("type", "double")
                        .endObject()
                        .startObject("stock")
                            .field("type", "integer")
                        .endObject()
                        .startObject("category")
                            .field("type", "keyword")
                        .endObject()
                        .startObject("createdAt")
                            .field("type", "long")
                        .endObject()
                        .startObject("updatedAt")
                            .field("type", "long")
                        .endObject()
                    .endObject()
                .endObject();
        
        request.mapping(builder);
        
        // 创建索引
        elasticsearchClient.indices().create(request, RequestOptions.DEFAULT);
        log.info("成功创建索引: products");
    }
    
    private List<ProductDocument> createTestProducts() {
        List<ProductDocument> products = new ArrayList<>();
        long now = System.currentTimeMillis();
        
        // 手机分类
        ProductDocument phone1 = new ProductDocument();
        phone1.setId(1L);
        phone1.setName("小米手机 11");
        phone1.setDescription("骁龙888处理器，120Hz高刷新率，5000mAh大电池");
        phone1.setPrice(new BigDecimal("3999.00"));
        phone1.setStock(100);
        phone1.setCategory("手机");
        phone1.setCreatedAt(now);
        phone1.setUpdatedAt(now);
        products.add(phone1);
        
        ProductDocument phone2 = new ProductDocument();
        phone2.setId(2L);
        phone2.setName("华为P50 Pro");
        phone2.setDescription("麒麟9000处理器，IP68防水，徕卡四摄");
        phone2.setPrice(new BigDecimal("6488.00"));
        phone2.setStock(50);
        phone2.setCategory("手机");
        phone2.setCreatedAt(now);
        phone2.setUpdatedAt(now);
        products.add(phone2);
        
        // 笔记本分类
        ProductDocument laptop1 = new ProductDocument();
        laptop1.setId(3L);
        laptop1.setName("联想小新Pro 14");
        laptop1.setDescription("AMD R7 5800H，16GB内存，512GB固态硬盘");
        laptop1.setPrice(new BigDecimal("5699.00"));
        laptop1.setStock(30);
        laptop1.setCategory("笔记本");
        laptop1.setCreatedAt(now);
        laptop1.setUpdatedAt(now);
        products.add(laptop1);
        
        ProductDocument laptop2 = new ProductDocument();
        laptop2.setId(4L);
        laptop2.setName("MacBook Pro 13");
        laptop2.setDescription("M1芯片，8GB内存，256GB固态硬盘");
        laptop2.setPrice(new BigDecimal("9999.00"));
        laptop2.setStock(20);
        laptop2.setCategory("笔记本");
        laptop2.setCreatedAt(now);
        laptop2.setUpdatedAt(now);
        products.add(laptop2);
        
        // 耳机分类
        ProductDocument headphone1 = new ProductDocument();
        headphone1.setId(5L);
        headphone1.setName("AirPods Pro");
        headphone1.setDescription("主动降噪，IPX4防水，无线充电");
        headphone1.setPrice(new BigDecimal("1999.00"));
        headphone1.setStock(200);
        headphone1.setCategory("耳机");
        headphone1.setCreatedAt(now);
        headphone1.setUpdatedAt(now);
        products.add(headphone1);
        
        return products;
    }
} 