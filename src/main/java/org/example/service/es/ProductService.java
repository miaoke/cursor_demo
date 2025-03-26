package org.example.service.es;

import org.example.entity.es.ProductDocument;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductDocument save(ProductDocument product);
    
    void delete(Long id);
    
    Optional<ProductDocument> findById(Long id);
    
    List<ProductDocument> findAll();
    
    List<ProductDocument> findByName(String name);
    
    List<ProductDocument> findByCategory(String category);
    
    List<ProductDocument> findByPriceRange(double minPrice, double maxPrice);
    
    void deleteAll();
} 