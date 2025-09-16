package com.example.orderup.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void notFound_shouldReturn404() {
        NotFoundException ex = new NotFoundException("Product missing");
        ResponseEntity<ApiError> response = handler.handleNotFound(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Product missing");
        assertThat(response.getBody().getError()).isEqualTo("Not Found");
        assertThat(response.getBody().getStatus()).isEqualTo(404);
    }

    @Test
    void insufficientStock_shouldReturn400() {
        InsufficientStockException ex = new InsufficientStockException("Out of stock");
        ResponseEntity<ApiError> response = handler.handleInsufficientStock(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Out of stock");
        assertThat(response.getBody().getError()).isEqualTo("Insufficient Stock");
        assertThat(response.getBody().getStatus()).isEqualTo(400);
    }

    @Test
    void generic_shouldReturn500() {
        Exception ex = new Exception("Unexpected");
        ResponseEntity<ApiError> response = handler.handleGenericException(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Unexpected");
        assertThat(response.getBody().getError()).isEqualTo("Server Error");
        assertThat(response.getBody().getStatus()).isEqualTo(500);
    }
}
