package com.example.orderup.service;

import com.example.orderup.entity.OrderEntity;
import com.example.orderup.entity.Product;
import com.example.orderup.exception.InsufficientStockException;
import com.example.orderup.exception.NotFoundException;
import com.example.orderup.repository.OrderRepository;
import com.example.orderup.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Places an order for a product. Only one order should succeed when stock is 1 and two concurrent calls.
     */
    @Transactional  // ✅ Add this to ensure each call runs in its own transaction
    public OrderEntity placeOrder(Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        return reserveProductAndCreateOrder(productId, quantity);
    }

    protected OrderEntity reserveProductAndCreateOrder(Long productId, int quantity) {
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found with ID: " + productId);
        }

        if (product.getStock() < quantity) {
            throw new InsufficientStockException(
                    "Not enough stock available. Requested: " + quantity + ", Available: " + product.getStock()
            );
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        productRepository.flush();  // make sure the change is committed to the DB

        OrderEntity order = new OrderEntity(quantity, "RESERVED");
        product.addOrder(order);

        return orderRepository.save(order);
    }
}
