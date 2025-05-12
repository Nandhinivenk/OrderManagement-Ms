package com.cloudkitchen.foodingredientmapping.client;

import com.cloudkitchen.foodingredientmapping.dto.FoodItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "food-catalog-service")
public interface FoodCatalogClient {
    
    @GetMapping("/api/food-items/{id}")
    ResponseEntity<FoodItemDTO> getFoodItemById(@PathVariable("id") Long id);
}
