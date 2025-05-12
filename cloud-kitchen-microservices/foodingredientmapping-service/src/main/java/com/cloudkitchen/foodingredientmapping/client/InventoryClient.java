package com.cloudkitchen.foodingredientmapping.client;

import com.cloudkitchen.foodingredientmapping.dto.InventoryItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    
    @GetMapping("/api/inventory/{id}")
    ResponseEntity<InventoryItemDTO> getInventoryItemById(@PathVariable("id") Long id);
    
    @PatchMapping("/api/inventory/{id}/quantity")
    ResponseEntity<InventoryItemDTO> updateInventoryItemQuantity(@PathVariable("id") Long id, @RequestBody Map<String, Integer> request);
}
