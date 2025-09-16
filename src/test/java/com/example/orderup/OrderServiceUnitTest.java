package com.example.orderup;

import com.example.orderup.entity.Product;
import com.example.orderup.exception.InsufficientStockException;
import com.example.orderup.repository.OrderRepository;
import com.example.orderup.repository.ProductRepository;
import com.example.orderup.service.OrderService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceUnitTest {

    @Test
    void placeOrder_success() {
        // Arrange
        ProductRepository productRepo = mock(ProductRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);
        Product product = new Product("Vase", 5);

        when(productRepo.findProductById(1L)).thenReturn((product));
        when(productRepo.save(any())).thenReturn(product);
        when(orderRepo.save(any())).thenAnswer(inv -> inv.getArgument(0)); // Return passed order

        OrderService service = new OrderService(productRepo, orderRepo);

        // Act
        var order = service.placeOrder(1L, 2);

        // Assert
        assertEquals(2, order.getQuantity());
        assertEquals("Vase", order.getProduct().getName());
    }

    @Test
    void placeOrder_insufficientStock_throwsException() {
        // Arrange
        ProductRepository productRepo = mock(ProductRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);
        Product product = new Product("Cup", 1);

        when(productRepo.findProductById(2L)).thenReturn((product));

        OrderService service = new OrderService(productRepo, orderRepo);

        // Act & Assert
        assertThrows(InsufficientStockException.class, () -> service.placeOrder(2L, 3));
    }
}
