package com.cloudkitchen.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "kitchenflow-service")
public interface KitchenFlowClient {
    
    @PostMapping("/api/kitchen-flow/orders/{orderId}/initialize")
    ResponseEntity<?> initializeOrderFlow(@PathVariable("orderId") Long orderId);
}
