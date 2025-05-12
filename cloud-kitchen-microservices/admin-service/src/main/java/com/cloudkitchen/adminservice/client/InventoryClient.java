package com.cloudkitchen.adminservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    
    @GetMapping("/api/inventory")
    ResponseEntity<List<Map<String, Object>>> getAllInventoryItems();
    
    @GetMapping("/api/inventory/{id}")
    ResponseEntity<Map<String, Object>> getInventoryItemById(@PathVariable("id") Long id);
    
    @PatchMapping("/api/inventory/{id}/quantity")
    ResponseEntity<Map<String, Object>> updateInventoryQuantity(
            @PathVariable("id") Long id, 
            @RequestBody Map<String, Integer> quantityUpdate);
}
