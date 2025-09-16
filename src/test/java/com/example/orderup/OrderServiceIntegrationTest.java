package com.example.orderup;

import com.example.orderup.entity.Product;
import com.example.orderup.repository.ProductRepository;
import com.example.orderup.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderServiceIntegrationTest {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderService orderService;

    private ExecutorService executor;

    @BeforeEach
    void setup() {
        productRepo.deleteAll();
        executor = Executors.newFixedThreadPool(2);
    }

    @AfterEach
    void teardown() throws InterruptedException {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        }
    }

    @Test
    void concurrentOnlyOneSucceeds() throws Exception {
        Product product = productRepo.save(new Product("Rare", 1));
        productRepo.flush();  // Flush so product is committed to DB

        Callable<Boolean> task = () -> {
            try {
                orderService.placeOrder(product.getId(), 1);
                return true;
            } catch (Exception e) {
                // Print stack trace if needed
                System.out.println("Order failed: " + e.getMessage());
                return false;
            }
        };

        var results = executor.invokeAll(List.of(task, task));

        int successCount = 0;
        for (var future : results) {
            if (future.get()) successCount++;
        }

        assertEquals(1, successCount, "Only one order should succeed due to stock=1");
    }
}
