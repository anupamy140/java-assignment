package com.example.orderup.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestErrorController {

    @GetMapping("/test/error/product-not-found")
    public void throwProductNotFound() {
        throw new NotFoundException("Product not found");
    }

    @GetMapping("/test/error/out-of-stock")
    public void throwOutOfStock() {
        throw new InsufficientStockException("Out of stock");
    }

    @GetMapping("/test/error/illegal-arg")
    public void throwIllegalArg() {
        throw new IllegalArgumentException("Invalid input");
    }

    @GetMapping("/test/error/generic")
    public void throwGeneric() throws Exception {
        throw new Exception("Unexpected");
    }
}
