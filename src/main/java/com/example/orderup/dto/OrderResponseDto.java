package com.example.orderup.dto;

import java.time.Instant;

/**
 * DTO representing a successful order response.
 */
public class OrderResponseDto {

    private Long id;
    private int quantity;
    private Instant createdAt;
    private String status;
    private ProductDto product;

    public OrderResponseDto() {
        // Default constructor
    }

    public OrderResponseDto(Long id, int quantity, Instant createdAt, String status, ProductDto product) {
        this.id = id;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.status = status;
        this.product = product;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public ProductDto getProduct() {
        return product;
    }

    // Nested Product DTO
    public static class ProductDto {
        private Long id;
        private String name;
        private int stock;

        public ProductDto() {
            // Default constructor
        }

        public ProductDto(Long id, String name, int stock) {
            this.id = id;
            this.name = name;
            this.stock = stock;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getStock() {
            return stock;
        }
    }
}
