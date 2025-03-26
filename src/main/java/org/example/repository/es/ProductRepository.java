package org.example.repository.es;

import org.example.entity.es.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<ProductDocument, Long> {
    
    List<ProductDocument> findByNameContaining(String name);
    
    List<ProductDocument> findByCategory(String category);
    
    List<ProductDocument> findByPriceBetween(double minPrice, double maxPrice);
} 