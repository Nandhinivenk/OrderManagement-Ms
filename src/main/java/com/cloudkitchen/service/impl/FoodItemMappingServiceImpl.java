package com.cloudkitchen.service.impl;

import com.cloudkitchen.model.FoodItem;
import com.cloudkitchen.model.FoodItemMapping;
import com.cloudkitchen.repository.FoodItemMappingRepository;
import com.cloudkitchen.repository.FoodItemRepository;
import com.cloudkitchen.repository.impl.FoodItemMappingRepositoryImpl;
import com.cloudkitchen.repository.impl.FoodItemRepositoryImpl;
import com.cloudkitchen.service.FoodItemMappingService;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of FoodItemMappingService
 */
public class FoodItemMappingServiceImpl implements FoodItemMappingService {
    
    private final FoodItemMappingRepository foodItemMappingRepository;
    private final FoodItemRepository foodItemRepository;
    
    public FoodItemMappingServiceImpl() {
        this.foodItemMappingRepository = new FoodItemMappingRepositoryImpl();
        this.foodItemRepository = new FoodItemRepositoryImpl();
    }
    
    @Override
    public FoodItemMapping createMapping(int foodItemId, int categoryId) {
        // Check if the food item exists
        Optional<FoodItem> foodItemOpt = foodItemRepository.findById(foodItemId);
        
        if (foodItemOpt.isEmpty()) {
            throw new IllegalArgumentException("Food item not found");
        }
        
        // Check if the mapping already exists
        List<FoodItemMapping> existingMappings = foodItemMappingRepository.findByFoodItemId(foodItemId);
        
        for (FoodItemMapping mapping : existingMappings) {
            if (mapping.getCategoryId() == categoryId) {
                throw new IllegalArgumentException("This food item is already mapped to this category");
            }
        }
        
        // Create a new mapping
        FoodItemMapping mapping = new FoodItemMapping(foodItemId, categoryId);
        
        return foodItemMappingRepository.save(mapping);
    }
    
    @Override
    public Optional<FoodItemMapping> getMappingById(int id) {
        return foodItemMappingRepository.findById(id);
    }
    
    @Override
    public List<FoodItemMapping> getAllMappings() {
        return foodItemMappingRepository.findAll();
    }
    
    @Override
    public List<FoodItemMapping> getMappingsByFoodItemId(int foodItemId) {
        return foodItemMappingRepository.findByFoodItemId(foodItemId);
    }
    
    @Override
    public List<FoodItemMapping> getMappingsByCategoryId(int categoryId) {
        return foodItemMappingRepository.findByCategoryId(categoryId);
    }
    
    @Override
    public FoodItemMapping updateMapping(int mappingId, int foodItemId, int categoryId) {
        // Check if the mapping exists
        Optional<FoodItemMapping> mappingOpt = foodItemMappingRepository.findById(mappingId);
        
        if (mappingOpt.isEmpty()) {
            throw new IllegalArgumentException("Food item mapping not found");
        }
        
        // Check if the food item exists
        Optional<FoodItem> foodItemOpt = foodItemRepository.findById(foodItemId);
        
        if (foodItemOpt.isEmpty()) {
            throw new IllegalArgumentException("Food item not found");
        }
        
        // Check if the mapping already exists
        List<FoodItemMapping> existingMappings = foodItemMappingRepository.findByFoodItemId(foodItemId);
        
        for (FoodItemMapping mapping : existingMappings) {
            if (mapping.getCategoryId() == categoryId && mapping.getId() != mappingId) {
                throw new IllegalArgumentException("This food item is already mapped to this category");
            }
        }
        
        // Update the mapping
        FoodItemMapping mapping = mappingOpt.get();
        mapping.setFoodItemId(foodItemId);
        mapping.setCategoryId(categoryId);
        
        return foodItemMappingRepository.update(mapping);
    }
    
    @Override
    public boolean deleteMapping(int mappingId) {
        return foodItemMappingRepository.deleteById(mappingId);
    }
    
    @Override
    public boolean deleteMappingsByFoodItemId(int foodItemId) {
        return foodItemMappingRepository.deleteByFoodItemId(foodItemId);
    }
    
    @Override
    public boolean deleteMappingsByCategoryId(int categoryId) {
        return foodItemMappingRepository.deleteByCategoryId(categoryId);
    }
}
