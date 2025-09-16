package com.example.orderup.controller;

import com.example.orderup.controlller.OrderController;
import com.example.orderup.dto.OrderRequest;
import com.example.orderup.entity.OrderEntity;
import com.example.orderup.entity.Product;
import com.example.orderup.exception.GlobalExceptionHandler;
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

import java.lang.reflect.Field;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(GlobalExceptionHandler.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void placeOrder_success() throws Exception {
        // Arrange request
        OrderRequest request = new OrderRequest(1L, 2);

        // Build Product entity
        Product product = new Product("Handcrafted Vase", 3);

        // Build OrderEntity
        OrderEntity order = new OrderEntity(2, "RESERVED");
        order.setProduct(product);

        // Use reflection to set the ID manually for testing
        Field idField = OrderEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(order, 1L);

        // Mock service to return the entity
        Mockito.when(orderService.placeOrder(1L, 2)).thenReturn(order);

        // Act + Assert
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("RESERVED"))
                .andExpect(jsonPath("$.product.name").value("Handcrafted Vase"));
    }
}
