package com.cloudkitchen.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "order-service")
public interface OrderClient {
    
    @GetMapping("/api/orders")
    ResponseEntity<List<Map<String, Object>>> getAllOrders();
    
    @GetMapping("/api/orders/{id}")
    ResponseEntity<Map<String, Object>> getOrderById(@PathVariable("id") Long id);
    
    @PatchMapping("/api/orders/{id}/status")
    ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable("id") Long id, 
            @RequestBody Map<String, String> statusUpdate);
}
