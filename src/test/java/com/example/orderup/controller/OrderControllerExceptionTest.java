package com.example.orderup.controller;

import com.example.orderup.controlller.OrderController;
import com.example.orderup.dto.OrderRequest;
import com.example.orderup.exception.GlobalExceptionHandler;
import com.example.orderup.exception.InsufficientStockException;
import com.example.orderup.exception.NotFoundException;
import com.example.orderup.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@Import(GlobalExceptionHandler.class)
class OrderControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void placeOrder_productNotFound_returns404() throws Exception {
        Mockito.when(orderService.placeOrder(eq(999L), eq(1)))
                .thenThrow(new NotFoundException("Product not found: 999"));

        OrderRequest req = new OrderRequest(999L, 1);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found: 999"));
    }

    @Test
    void placeOrder_outOfStock_returns400() throws Exception {
        Mockito.when(orderService.placeOrder(eq(1L), eq(10)))
                .thenThrow(new InsufficientStockException("Not enough stock for product 1"));

        OrderRequest req = new OrderRequest(1L, 10);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Not enough stock for product 1"));
    }
}
