package com.cloudkitchen.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "foodingredientmapping-service")
public interface FoodIngredientMappingClient {
    
    @GetMapping("/api/food-ingredient-mappings/check-availability/{foodItemId}")
    ResponseEntity<Map<String, Boolean>> checkInventoryAvailability(
            @PathVariable("foodItemId") Long foodItemId,
            @RequestParam("quantity") int quantity);
    
    @PostMapping("/api/food-ingredient-mappings/update-inventory/{foodItemId}")
    ResponseEntity<Void> updateInventoryAfterOrder(
            @PathVariable("foodItemId") Long foodItemId,
            @RequestParam("quantity") int quantity);
}
