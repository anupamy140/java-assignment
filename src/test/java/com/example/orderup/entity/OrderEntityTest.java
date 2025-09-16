package com.example.orderup.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OrderEntityTest {

    private OrderEntity order;

    @BeforeEach
    void setUp() {
        order = new OrderEntity();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(order);
    }

    @Test
    void testParameterizedConstructor() {
        OrderEntity order2 = new OrderEntity(5, "NEW");
        assertEquals(5, order2.getQuantity());
        assertEquals("NEW", order2.getStatus());
    }

    @Test
    void testGettersAndSetters() {
        order.setQuantity(10);
        assertEquals(10, order.getQuantity());

        order.setStatus("SHIPPED");
        assertEquals("SHIPPED", order.getStatus());

        Product product = new Product("TestProduct", 20);
        order.setProduct(product);
        assertEquals(product, order.getProduct());
    }

    @Test
    void testOnCreateSetsCreatedAt() {
        assertNull(order.getCreatedAt());
        order.onCreate();
        assertNotNull(order.getCreatedAt());
        // Check that createdAt is recent (within last 2 seconds)
        assertTrue(Instant.now().minusSeconds(2).isBefore(order.getCreatedAt()));
    }

    @Test
    void testEqualsAndHashCode() throws Exception {
        OrderEntity order1 = new OrderEntity();
        OrderEntity order2 = new OrderEntity();

        // Use reflection to set private id fields
        java.lang.reflect.Field idField = OrderEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(order1, 1L);
        idField.set(order2, 1L);

        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());

        // Different id
        idField.set(order2, 2L);
        assertNotEquals(order1, order2);
        assertNotEquals(order1.hashCode(), order2.hashCode());

        // Null id test
        idField.set(order1, null);
        assertNotEquals(order1, order2);
        assertNotEquals(order1.hashCode(), order2.hashCode());

        // Same object
        assertEquals(order1, order1);
        assertNotEquals(order1, null);
        assertNotEquals(order1, new Object());
    }

    @Test
    void testToString() {
        order.setQuantity(3);
        order.setStatus("PENDING");

        Product product = new Product("Prod", 10);
        // Set id on product via reflection
        try {
            java.lang.reflect.Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(product, 5L);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }

        order.setProduct(product);

        String toString = order.toString();
        assertTrue(toString.contains("quantity=3"));
        assertTrue(toString.contains("status='PENDING'"));
        assertTrue(toString.contains("product=5"));
    }
}
