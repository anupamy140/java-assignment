package com.example.orderup.controller;

import com.example.orderup.entity.Product;
import com.example.orderup.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(com.example.orderup.controller.ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void createProduct_success() throws Exception {
        // Arrange: Build product
        Product product = new Product("Rare", 5);

        // Inject ID manually for testing
        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        // Mock repository save
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        // Act + Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rare"))
                .andExpect(jsonPath("$.stock").value(5));
    }

    @Test
    void getAllProducts_success() throws Exception {
        // Arrange: Prepare product list
        Product product = new Product("Rare", 5);

        // Inject ID
        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        Mockito.when(productRepository.findAll()).thenReturn(List.of(product));

        // Act + Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Rare"))
                .andExpect(jsonPath("$[0].stock").value(5));
    }
}
