package com.example.orderup.controller;

import com.example.orderup.dto.ProductResponseDto;
import com.example.orderup.entity.Product;
import com.example.orderup.repository.ProductRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    /**
     * Create a new product.
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody Product product) {
        Product saved = productRepo.save(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToDto(saved));
    }

    /**
     * Batch creation of products.
     */
    @PostMapping("/batch")
    public ResponseEntity<List<ProductResponseDto>> createBatch(@RequestBody List<@Valid Product> products) {
        List<Product> savedProducts = productRepo.saveAll(products);
        List<ProductResponseDto> responses = savedProducts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responses);
    }

    /**
     * Get all products.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> all() {
        List<ProductResponseDto> products = productRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

    /**
     * Get product by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        return productRepo.findById(id)
                .map(product -> ResponseEntity.ok(mapToDto(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Map internal Product entity to ProductResponseDto.
     */
    private ProductResponseDto mapToDto(Product p) {
        return new ProductResponseDto(p.getId(), p.getName(), p.getStock());
    }
}
