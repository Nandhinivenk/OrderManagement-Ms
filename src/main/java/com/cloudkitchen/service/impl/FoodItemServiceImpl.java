package com.cloudkitchen.service.impl;

import com.cloudkitchen.model.FoodItem;
import com.cloudkitchen.repository.FoodItemRepository;
import com.cloudkitchen.repository.impl.FoodItemRepositoryImpl;
import com.cloudkitchen.service.FoodItemService;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of FoodItemService
 */
public class FoodItemServiceImpl implements FoodItemService {
    
    private final FoodItemRepository foodItemRepository;
    
    public FoodItemServiceImpl() {
        this.foodItemRepository = new FoodItemRepositoryImpl();
    }
    
    @Override
    public FoodItem addFoodItem(FoodItem foodItem) {
        // Check if a food item with the same name already exists
        Optional<FoodItem> existingItem = foodItemRepository.findByName(foodItem.getName());
        
        if (existingItem.isPresent()) {
            throw new IllegalArgumentException("A food item with this name already exists");
        }
        
        return foodItemRepository.save(foodItem);
    }
    
    @Override
    public Optional<FoodItem> getFoodItemById(int id) {
        return foodItemRepository.findById(id);
    }
    
    @Override
    public Optional<FoodItem> getFoodItemByName(String name) {
        return foodItemRepository.findByName(name);
    }
    
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }
    
    @Override
    public List<FoodItem> getFoodItemsByCategory(String category) {
        return foodItemRepository.findByCategory(category);
    }
    
    @Override
    public List<FoodItem> getAvailableFoodItems() {
        return foodItemRepository.findAvailable();
    }
    
    @Override
    public FoodItem updateFoodItem(FoodItem foodItem) {
        // Check if the food item exists
        Optional<FoodItem> existingItem = foodItemRepository.findById(foodItem.getId());
        
        if (existingItem.isEmpty()) {
            throw new IllegalArgumentException("Food item not found");
        }
        
        // Check if the name is changed and if it conflicts with another item
        if (!existingItem.get().getName().equals(foodItem.getName())) {
            Optional<FoodItem> itemWithSameName = foodItemRepository.findByName(foodItem.getName());
            
            if (itemWithSameName.isPresent() && itemWithSameName.get().getId() != foodItem.getId()) {
                throw new IllegalArgumentException("A food item with this name already exists");
            }
        }
        
        return foodItemRepository.update(foodItem);
    }
    
    @Override
    public FoodItem updateFoodItemAvailability(int id, boolean isAvailable) {
        Optional<FoodItem> itemOpt = foodItemRepository.findById(id);
        
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Food item not found");
        }
        
        FoodItem foodItem = itemOpt.get();
        foodItem.setAvailable(isAvailable);
        
        return foodItemRepository.update(foodItem);
    }
    
    @Override
    public boolean deleteFoodItem(int id) {
        return foodItemRepository.deleteById(id);
    }
}
