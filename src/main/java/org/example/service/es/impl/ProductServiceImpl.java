package org.example.service.es.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.es.ProductDocument;
import org.example.repository.es.ProductRepository;
import org.example.service.es.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductDocument save(ProductDocument product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<ProductDocument> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<ProductDocument> findAll() {
        List<ProductDocument> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    @Override
    public List<ProductDocument> findByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    @Override
    public List<ProductDocument> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<ProductDocument> findByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }
} 