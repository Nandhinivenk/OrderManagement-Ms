package com.cloudkitchen.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {
    
    @GetMapping("/api/deliveries")
    ResponseEntity<List<Map<String, Object>>> getAllDeliveries();
    
    @GetMapping("/api/deliveries/{id}")
    ResponseEntity<Map<String, Object>> getDeliveryById(@PathVariable("id") Long id);
    
    @GetMapping("/api/deliveries/order/{orderId}")
    ResponseEntity<Map<String, Object>> getDeliveryByOrderId(@PathVariable("orderId") Long orderId);
    
    @PatchMapping("/api/deliveries/{id}/assign")
    ResponseEntity<Map<String, Object>> assignDeliveryPerson(
            @PathVariable("id") Long id, 
            @RequestBody Map<String, String> assignRequest);
    
    @PatchMapping("/api/deliveries/{id}/status")
    ResponseEntity<Map<String, Object>> updateDeliveryStatus(
            @PathVariable("id") Long id, 
            @RequestBody Map<String, String> statusUpdate);
}
