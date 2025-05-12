package com.cloudkitchen.deliveryservice.client;

import com.cloudkitchen.deliveryservice.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service")
public interface OrderClient {
    
    @GetMapping("/api/orders/{id}")
    ResponseEntity<OrderDTO> getOrderById(@PathVariable("id") Long id);
    
    @PatchMapping("/api/orders/{id}/status")
    ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable("id") Long id, @RequestParam String status);
}
