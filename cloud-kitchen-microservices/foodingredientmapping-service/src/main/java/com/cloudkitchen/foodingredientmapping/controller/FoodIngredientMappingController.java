package com.cloudkitchen.foodingredientmapping.controller;

import com.cloudkitchen.foodingredientmapping.dto.FoodIngredientMappingDTO;
import com.cloudkitchen.foodingredientmapping.service.FoodIngredientMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/food-ingredient-mappings")
@RequiredArgsConstructor
public class FoodIngredientMappingController {
    
    private final FoodIngredientMappingService mappingService;
    
    @PostMapping
    public ResponseEntity<FoodIngredientMappingDTO> addMapping(@Valid @RequestBody FoodIngredientMappingDTO mappingDTO) {
        FoodIngredientMappingDTO createdMapping = mappingService.addMapping(mappingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMapping);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FoodIngredientMappingDTO> getMappingById(@PathVariable Long id) {
        FoodIngredientMappingDTO mapping = mappingService.getMappingById(id);
        return ResponseEntity.ok(mapping);
    }
    
    @GetMapping("/food-item/{foodItemId}")
    public ResponseEntity<List<FoodIngredientMappingDTO>> getMappingsForFoodItem(@PathVariable Long foodItemId) {
        List<FoodIngredientMappingDTO> mappings = mappingService.getMappingsForFoodItem(foodItemId);
        return ResponseEntity.ok(mappings);
    }
    
    @GetMapping("/inventory-item/{inventoryItemId}")
    public ResponseEntity<List<FoodIngredientMappingDTO>> getMappingsForInventoryItem(@PathVariable Long inventoryItemId) {
        List<FoodIngredientMappingDTO> mappings = mappingService.getMappingsForInventoryItem(inventoryItemId);
        return ResponseEntity.ok(mappings);
    }
    
    @GetMapping
    public ResponseEntity<List<FoodIngredientMappingDTO>> getAllMappings() {
        List<FoodIngredientMappingDTO> mappings = mappingService.getAllMappings();
        return ResponseEntity.ok(mappings);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FoodIngredientMappingDTO> updateMapping(
            @PathVariable Long id, 
            @Valid @RequestBody FoodIngredientMappingDTO mappingDTO) {
        FoodIngredientMappingDTO updatedMapping = mappingService.updateMapping(id, mappingDTO);
        return ResponseEntity.ok(updatedMapping);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapping(@PathVariable Long id) {
        mappingService.deleteMapping(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/calculate/{foodItemId}")
    public ResponseEntity<Map<Long, Double>> calculateRequiredInventory(
            @PathVariable Long foodItemId,
            @RequestParam(defaultValue = "1") int quantity) {
        Map<Long, Double> requiredInventory = mappingService.calculateRequiredInventoryForOrder(foodItemId, quantity);
        return ResponseEntity.ok(requiredInventory);
    }
    
    @GetMapping("/check-availability/{foodItemId}")
    public ResponseEntity<Map<String, Boolean>> checkInventoryAvailability(
            @PathVariable Long foodItemId,
            @RequestParam(defaultValue = "1") int quantity) {
        boolean available = mappingService.checkInventoryAvailabilityForFoodItem(foodItemId, quantity);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/update-inventory/{foodItemId}")
    public ResponseEntity<Void> updateInventoryAfterOrder(
            @PathVariable Long foodItemId,
            @RequestParam(defaultValue = "1") int quantity) {
        mappingService.updateInventoryAfterOrderPlaced(foodItemId, quantity);
        return ResponseEntity.ok().build();
    }
}
