package com.example.orderup.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;
    private OrderEntity order;

    @BeforeEach
    void setUp() {
        product = new Product("TestProduct", 10);
        order = new OrderEntity(5, "NEW");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("TestProduct", product.getName());
        assertEquals(10, product.getStock());
        assertNotNull(product.getOrders());
        assertTrue(product.getOrders().isEmpty());
    }

    @Test
    void testSetName() {
        product.setName("NewName");
        assertEquals("NewName", product.getName());
    }

    @Test
    void testSetStockValid() {
        product.setStock(20);
        assertEquals(20, product.getStock());
    }

    @Test
    void testSetStockNegativeThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setStock(-1);
        });
        assertEquals("Stock cannot be negative", exception.getMessage());
    }

    @Test
    void testAddOrder() {
        product.addOrder(order);
        assertTrue(product.getOrders().contains(order));
        assertEquals(product, order.getProduct());
    }

    @Test
    void testAddOrderNullThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.addOrder(null);
        });
        assertEquals("Order cannot be null", exception.getMessage());
    }

    @Test
    void testRemoveOrder() {
        product.addOrder(order);
        product.removeOrder(order);
        assertFalse(product.getOrders().contains(order));
        assertNull(order.getProduct());
    }

    @Test
    void testRemoveOrderNullSafe() {
        // Should not throw if null is passed
        assertDoesNotThrow(() -> product.removeOrder(null));
    }

    @Test
    void testHasSufficientStock() {
        assertTrue(product.hasSufficientStock(5));
        assertTrue(product.hasSufficientStock(10));
        assertFalse(product.hasSufficientStock(11));
    }

    @Test
    void testEqualsAndHashCode() throws Exception {
        Product p1 = new Product("Name", 10);
        Product p2 = new Product("Name", 10);

        // Use reflection to set private id field
        java.lang.reflect.Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);

        idField.set(p1, 1L);
        idField.set(p2, 1L);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());

        idField.set(p2, 2L);
        assertNotEquals(p1, p2);
    }

    @Test
    void testToStringContainsFields() {
        String str = product.toString();
        assertTrue(str.contains("TestProduct"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("version"));
    }
}
