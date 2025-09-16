package com.example.orderup.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for placing an order.
 */
public record OrderRequest(
        @NotNull(message = "Product ID cannot be null")
        Long productId,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity
) {}
