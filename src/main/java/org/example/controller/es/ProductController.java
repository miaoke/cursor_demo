package org.example.controller.es;

import lombok.RequiredArgsConstructor;
import org.example.entity.es.ProductDocument;
import org.example.service.es.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/es/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDocument> createProduct(@RequestBody ProductDocument product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDocument> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductDocument>> getAllProducts() {
        try {
            List<ProductDocument> products = productService.findAll();
            return ResponseEntity.ok(products != null ? products : Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<ProductDocument>> searchProductsByName(@RequestParam String name) {
        try {
            List<ProductDocument> products = productService.findByName(name);
            return ResponseEntity.ok(products != null ? products : Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<ProductDocument>> searchProductsByCategory(@RequestParam String category) {
        try {
            List<ProductDocument> products = productService.findByCategory(category);
            return ResponseEntity.ok(products != null ? products : Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/search/price")
    public ResponseEntity<List<ProductDocument>> searchProductsByPriceRange(
            @RequestParam double minPrice, 
            @RequestParam double maxPrice) {
        try {
            List<ProductDocument> products = productService.findByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(products != null ? products : Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDocument> updateProduct(
            @PathVariable Long id, 
            @RequestBody ProductDocument product) {
        return productService.findById(id)
                .map(existingProduct -> {
                    product.setId(id);
                    return ResponseEntity.ok(productService.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.findById(id)
                .map(product -> {
                    productService.delete(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllProducts() {
        productService.deleteAll();
        return ResponseEntity.noContent().build();
    }
} 