package com.example.orderup.controlller;

import com.example.orderup.dto.OrderRequest;
import com.example.orderup.dto.OrderResponseDto;
import com.example.orderup.entity.OrderEntity;
import com.example.orderup.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Place a single order for a product
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@Valid @RequestBody OrderRequest request) {
        OrderEntity order = orderService.placeOrder(request.productId(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(order));
    }

    /**
     * Place multiple orders in a batch
     */
    @PostMapping("/batch")
    public ResponseEntity<List<OrderResponseDto>> placeOrders(@RequestBody List<@Valid OrderRequest> requests) {
        List<OrderResponseDto> responses = requests.stream()
                .map(req -> mapToDto(orderService.placeOrder(req.productId(), req.quantity())))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    /**
     * Maps internal OrderEntity to external OrderResponseDto
     */
    private OrderResponseDto mapToDto(OrderEntity order) {
        return new OrderResponseDto(
                order.getId(),
                order.getQuantity(),
                order.getCreatedAt(),
                order.getStatus(),
                new OrderResponseDto.ProductDto(
                        order.getProduct().getId(),
                        order.getProduct().getName(),
                        order.getProduct().getStock()
                )
        );
    }
}
