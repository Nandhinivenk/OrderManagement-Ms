package com.cloudkitchen.kitchenflowservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {
    
    @PatchMapping("/api/deliveries/order/{orderId}/assign")
    ResponseEntity<?> assignDeliveryPerson(@PathVariable("orderId") Long orderId, @RequestBody Map<String, String> request);
    
    @PatchMapping("/api/deliveries/order/{orderId}/status")
    ResponseEntity<?> updateDeliveryStatus(@PathVariable("orderId") Long orderId, @RequestBody Map<String, String> request);
}
