package com.cloudkitchen.foodcatalogservice.controller;

import com.cloudkitchen.foodcatalogservice.dto.FoodItemDTO;
import com.cloudkitchen.foodcatalogservice.service.FoodItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/food-items")
@RequiredArgsConstructor
public class FoodItemController {
    
    private final FoodItemService foodItemService;
    
    @PostMapping
    public ResponseEntity<FoodItemDTO> createFoodItem(@Valid @RequestBody FoodItemDTO foodItemDTO) {
        FoodItemDTO createdFoodItem = foodItemService.createFoodItem(foodItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFoodItem);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDTO> getFoodItemById(@PathVariable Long id) {
        FoodItemDTO foodItem = foodItemService.getFoodItemById(id);
        return ResponseEntity.ok(foodItem);
    }
    
    @GetMapping
    public ResponseEntity<List<FoodItemDTO>> getAllFoodItems() {
        List<FoodItemDTO> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<FoodItemDTO>> getFoodItemsByCategory(@PathVariable Long categoryId) {
        List<FoodItemDTO> foodItems = foodItemService.getFoodItemsByCategory(categoryId);
        return ResponseEntity.ok(foodItems);
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<FoodItemDTO>> getAvailableFoodItems() {
        List<FoodItemDTO> foodItems = foodItemService.getAvailableFoodItems();
        return ResponseEntity.ok(foodItems);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<FoodItemDTO>> searchFoodItems(@RequestParam String keyword) {
        List<FoodItemDTO> foodItems = foodItemService.searchFoodItems(keyword);
        return ResponseEntity.ok(foodItems);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FoodItemDTO> updateFoodItem(@PathVariable Long id, @Valid @RequestBody FoodItemDTO foodItemDTO) {
        FoodItemDTO updatedFoodItem = foodItemService.updateFoodItem(id, foodItemDTO);
        return ResponseEntity.ok(updatedFoodItem);
    }
    
    @PatchMapping("/{id}/availability")
    public ResponseEntity<FoodItemDTO> updateFoodItemAvailability(@PathVariable Long id, @RequestParam boolean available) {
        FoodItemDTO updatedFoodItem = foodItemService.updateFoodItemAvailability(id, available);
        return ResponseEntity.ok(updatedFoodItem);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        foodItemService.deleteFoodItem(id);
        return ResponseEntity.noContent().build();
    }
}
