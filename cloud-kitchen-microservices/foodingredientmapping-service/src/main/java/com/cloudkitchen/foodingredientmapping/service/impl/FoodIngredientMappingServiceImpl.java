package com.cloudkitchen.foodingredientmapping.service.impl;

import com.cloudkitchen.foodingredientmapping.client.FoodCatalogClient;
import com.cloudkitchen.foodingredientmapping.client.InventoryClient;
import com.cloudkitchen.foodingredientmapping.dto.FoodIngredientMappingDTO;
import com.cloudkitchen.foodingredientmapping.dto.FoodItemDTO;
import com.cloudkitchen.foodingredientmapping.dto.InventoryItemDTO;
import com.cloudkitchen.foodingredientmapping.exception.DuplicateMappingException;
import com.cloudkitchen.foodingredientmapping.exception.MappingNotFoundException;
import com.cloudkitchen.foodingredientmapping.exception.ResourceNotFoundException;
import com.cloudkitchen.foodingredientmapping.model.FoodIngredientMapping;
import com.cloudkitchen.foodingredientmapping.repository.FoodIngredientMappingRepository;
import com.cloudkitchen.foodingredientmapping.service.FoodIngredientMappingService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodIngredientMappingServiceImpl implements FoodIngredientMappingService {
    
    private final FoodIngredientMappingRepository mappingRepository;
    private final FoodCatalogClient foodCatalogClient;
    private final InventoryClient inventoryClient;
    
    @Override
    @Transactional
    public FoodIngredientMappingDTO addMapping(FoodIngredientMappingDTO mappingDTO) {
        // Check if mapping already exists
        if (mappingRepository.findByFoodItemIdAndInventoryItemId(
                mappingDTO.getFoodItemId(), mappingDTO.getInventoryItemId()).isPresent()) {
            throw new DuplicateMappingException("Mapping already exists for food item ID " + 
                    mappingDTO.getFoodItemId() + " and inventory item ID " + mappingDTO.getInventoryItemId());
        }
        
        // Fetch food item and inventory item details
        FoodItemDTO foodItem = getFoodItem(mappingDTO.getFoodItemId());
        InventoryItemDTO inventoryItem = getInventoryItem(mappingDTO.getInventoryItemId());
        
        // Create and save the mapping
        FoodIngredientMapping mapping = new FoodIngredientMapping();
        mapping.setFoodItemId(mappingDTO.getFoodItemId());
        mapping.setInventoryItemId(mappingDTO.getInventoryItemId());
        mapping.setQuantityRequired(mappingDTO.getQuantityRequired());
        mapping.setFoodItemName(foodItem.getName());
        mapping.setInventoryItemName(inventoryItem.getName());
        mapping.setInventoryItemUnit(inventoryItem.getUnit());
        
        FoodIngredientMapping savedMapping = mappingRepository.save(mapping);
        
        return mapToDTO(savedMapping);
    }
    
    @Override
    public FoodIngredientMappingDTO getMappingById(Long id) {
        FoodIngredientMapping mapping = mappingRepository.findById(id)
                .orElseThrow(() -> new MappingNotFoundException("Mapping not found with id: " + id));
        
        return mapToDTO(mapping);
    }
    
    @Override
    public List<FoodIngredientMappingDTO> getMappingsForFoodItem(Long foodItemId) {
        // Verify food item exists
        getFoodItem(foodItemId);
        
        return mappingRepository.findByFoodItemId(foodItemId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FoodIngredientMappingDTO> getMappingsForInventoryItem(Long inventoryItemId) {
        // Verify inventory item exists
        getInventoryItem(inventoryItemId);
        
        return mappingRepository.findByInventoryItemId(inventoryItemId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FoodIngredientMappingDTO> getAllMappings() {
        return mappingRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public FoodIngredientMappingDTO updateMapping(Long id, FoodIngredientMappingDTO mappingDTO) {
        FoodIngredientMapping mapping = mappingRepository.findById(id)
                .orElseThrow(() -> new MappingNotFoundException("Mapping not found with id: " + id));
        
        // Check if updating to a combination that already exists
        if (!mapping.getFoodItemId().equals(mappingDTO.getFoodItemId()) || 
                !mapping.getInventoryItemId().equals(mappingDTO.getInventoryItemId())) {
            
            if (mappingRepository.findByFoodItemIdAndInventoryItemId(
                    mappingDTO.getFoodItemId(), mappingDTO.getInventoryItemId()).isPresent()) {
                throw new DuplicateMappingException("Mapping already exists for food item ID " + 
                        mappingDTO.getFoodItemId() + " and inventory item ID " + mappingDTO.getInventoryItemId());
            }
            
            // Fetch updated food item and inventory item details
            FoodItemDTO foodItem = getFoodItem(mappingDTO.getFoodItemId());
            InventoryItemDTO inventoryItem = getInventoryItem(mappingDTO.getInventoryItemId());
            
            mapping.setFoodItemId(mappingDTO.getFoodItemId());
            mapping.setInventoryItemId(mappingDTO.getInventoryItemId());
            mapping.setFoodItemName(foodItem.getName());
            mapping.setInventoryItemName(inventoryItem.getName());
            mapping.setInventoryItemUnit(inventoryItem.getUnit());
        }
        
        mapping.setQuantityRequired(mappingDTO.getQuantityRequired());
        
        FoodIngredientMapping updatedMapping = mappingRepository.save(mapping);
        
        return mapToDTO(updatedMapping);
    }
    
    @Override
    @Transactional
    public void deleteMapping(Long id) {
        if (!mappingRepository.existsById(id)) {
            throw new MappingNotFoundException("Mapping not found with id: " + id);
        }
        
        mappingRepository.deleteById(id);
    }
    
    @Override
    public Map<Long, Double> calculateRequiredInventoryForOrder(Long foodItemId, int quantity) {
        List<FoodIngredientMapping> mappings = mappingRepository.findByFoodItemId(foodItemId);
        
        if (mappings.isEmpty()) {
            throw new ResourceNotFoundException("No ingredient mappings found for food item ID: " + foodItemId);
        }
        
        Map<Long, Double> requiredInventory = new HashMap<>();
        
        for (FoodIngredientMapping mapping : mappings) {
            double requiredQuantity = mapping.getQuantityRequired() * quantity;
            requiredInventory.put(mapping.getInventoryItemId(), requiredQuantity);
        }
        
        return requiredInventory;
    }
    
    @Override
    public boolean checkInventoryAvailabilityForFoodItem(Long foodItemId, int quantity) {
        Map<Long, Double> requiredInventory = calculateRequiredInventoryForOrder(foodItemId, quantity);
        
        for (Map.Entry<Long, Double> entry : requiredInventory.entrySet()) {
            Long inventoryItemId = entry.getKey();
            Double requiredQuantity = entry.getValue();
            
            InventoryItemDTO inventoryItem = getInventoryItem(inventoryItemId);
            
            if (inventoryItem.getQuantity() < requiredQuantity) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    @Transactional
    public void updateInventoryAfterOrderPlaced(Long foodItemId, int quantity) {
        Map<Long, Double> requiredInventory = calculateRequiredInventoryForOrder(foodItemId, quantity);
        
        for (Map.Entry<Long, Double> entry : requiredInventory.entrySet()) {
            Long inventoryItemId = entry.getKey();
            Double requiredQuantity = entry.getValue();
            
            InventoryItemDTO inventoryItem = getInventoryItem(inventoryItemId);
            
            int newQuantity = inventoryItem.getQuantity() - requiredQuantity.intValue();
            
            if (newQuantity < 0) {
                throw new IllegalStateException("Not enough inventory for item: " + inventoryItem.getName());
            }
            
            try {
                Map<String, Integer> request = new HashMap<>();
                request.put("quantity", newQuantity);
                inventoryClient.updateInventoryItemQuantity(inventoryItemId, request);
            } catch (FeignException e) {
                throw new RuntimeException("Failed to update inventory: " + e.getMessage());
            }
        }
    }
    
    private FoodItemDTO getFoodItem(Long foodItemId) {
        try {
            FoodItemDTO foodItem = foodCatalogClient.getFoodItemById(foodItemId).getBody();
            if (foodItem == null) {
                throw new ResourceNotFoundException("Food item not found with id: " + foodItemId);
            }
            return foodItem;
        } catch (FeignException e) {
            throw new ResourceNotFoundException("Food item not found with id: " + foodItemId);
        }
    }
    
    private InventoryItemDTO getInventoryItem(Long inventoryItemId) {
        try {
            InventoryItemDTO inventoryItem = inventoryClient.getInventoryItemById(inventoryItemId).getBody();
            if (inventoryItem == null) {
                throw new ResourceNotFoundException("Inventory item not found with id: " + inventoryItemId);
            }
            return inventoryItem;
        } catch (FeignException e) {
            throw new ResourceNotFoundException("Inventory item not found with id: " + inventoryItemId);
        }
    }
    
    private FoodIngredientMappingDTO mapToDTO(FoodIngredientMapping mapping) {
        FoodIngredientMappingDTO dto = new FoodIngredientMappingDTO();
        dto.setId(mapping.getId());
        dto.setFoodItemId(mapping.getFoodItemId());
        dto.setInventoryItemId(mapping.getInventoryItemId());
        dto.setQuantityRequired(mapping.getQuantityRequired());
        dto.setFoodItemName(mapping.getFoodItemName());
        dto.setInventoryItemName(mapping.getInventoryItemName());
        dto.setInventoryItemUnit(mapping.getInventoryItemUnit());
        return dto;
    }
}
