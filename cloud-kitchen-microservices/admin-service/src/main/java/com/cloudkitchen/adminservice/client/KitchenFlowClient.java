package com.cloudkitchen.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "kitchenflow-service")
public interface KitchenFlowClient {
    
    @GetMapping("/api/kitchen-flow/orders")
    ResponseEntity<List<Map<String, Object>>> getAllOrderFlows();
    
    @GetMapping("/api/kitchen-flow/orders/{orderId}")
    ResponseEntity<Map<String, Object>> getOrderFlowById(@PathVariable("orderId") Long orderId);
    
    @GetMapping("/api/kitchen-flow/orders/status/{status}")
    ResponseEntity<List<Map<String, Object>>> getOrderFlowsByStatus(@PathVariable("status") String status);
    
    @PatchMapping("/api/kitchen-flow/orders/{orderId}/status")
    ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable("orderId") Long orderId, 
            @RequestBody Map<String, String> statusUpdate);
}
