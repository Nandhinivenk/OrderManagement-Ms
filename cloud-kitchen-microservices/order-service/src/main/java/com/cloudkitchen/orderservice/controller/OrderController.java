package com.cloudkitchen.orderservice.controller;

import com.cloudkitchen.orderservice.dto.CreateOrderRequest;
import com.cloudkitchen.orderservice.dto.OrderDTO;
import com.cloudkitchen.orderservice.dto.OrderItemDTO;
import com.cloudkitchen.orderservice.dto.UpdateOrderStatusRequest;
import com.cloudkitchen.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        OrderDTO order = orderService.createOrder(createOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable String status) {
        List<OrderDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        OrderDTO order = orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok(order);
    }
    
    @PatchMapping("/{id}/payment-status")
    public ResponseEntity<OrderDTO> updatePaymentStatus(@PathVariable Long id, @RequestParam String paymentStatus) {
        OrderDTO order = orderService.updatePaymentStatus(id, paymentStatus);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{id}/items")
    public ResponseEntity<OrderDTO> addItemToOrder(@PathVariable Long id, @Valid @RequestBody OrderItemDTO orderItemDTO) {
        OrderDTO order = orderService.addItemToOrder(id, orderItemDTO);
        return ResponseEntity.ok(order);
    }
    
    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderDTO> removeItemFromOrder(@PathVariable Long orderId, @PathVariable Long itemId) {
        OrderDTO order = orderService.removeItemFromOrder(orderId, itemId);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id) {
        OrderDTO order = orderService.cancelOrder(id);
        return ResponseEntity.ok(order);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
