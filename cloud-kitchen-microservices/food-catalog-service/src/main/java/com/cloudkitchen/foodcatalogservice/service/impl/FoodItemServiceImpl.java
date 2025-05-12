package com.cloudkitchen.foodcatalogservice.service.impl;

import com.cloudkitchen.foodcatalogservice.dto.FoodItemDTO;
import com.cloudkitchen.foodcatalogservice.exception.CategoryNotFoundException;
import com.cloudkitchen.foodcatalogservice.exception.FoodItemNotFoundException;
import com.cloudkitchen.foodcatalogservice.model.Category;
import com.cloudkitchen.foodcatalogservice.model.FoodItem;
import com.cloudkitchen.foodcatalogservice.repository.CategoryRepository;
import com.cloudkitchen.foodcatalogservice.repository.FoodItemRepository;
import com.cloudkitchen.foodcatalogservice.service.FoodItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodItemServiceImpl implements FoodItemService {
    
    private final FoodItemRepository foodItemRepository;
    private final CategoryRepository categoryRepository;
    
    @Override
    public FoodItemDTO createFoodItem(FoodItemDTO foodItemDTO) {
        // Check if category exists
        Category category = null;
        if (foodItemDTO.getCategoryId() != null) {
            category = categoryRepository.findById(foodItemDTO.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + foodItemDTO.getCategoryId()));
        }
        
        // Create new food item
        FoodItem foodItem = new FoodItem();
        foodItem.setName(foodItemDTO.getName());
        foodItem.setDescription(foodItemDTO.getDescription());
        foodItem.setPrice(foodItemDTO.getPrice());
        foodItem.setAvailable(foodItemDTO.isAvailable());
        foodItem.setCategory(category);
        
        // Save food item
        FoodItem savedFoodItem = foodItemRepository.save(foodItem);
        
        // Return DTO
        return mapToDTO(savedFoodItem);
    }
    
    @Override
    public FoodItemDTO getFoodItemById(Long id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new FoodItemNotFoundException("Food item not found with id: " + id));
        
        return mapToDTO(foodItem);
    }
    
    @Override
    public List<FoodItemDTO> getAllFoodItems() {
        return foodItemRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FoodItemDTO> getFoodItemsByCategory(Long categoryId) {
        // Check if category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found with id: " + categoryId);
        }
        
        return foodItemRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FoodItemDTO> getAvailableFoodItems() {
        return foodItemRepository.findByAvailableTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FoodItemDTO> searchFoodItems(String keyword) {
        return foodItemRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public FoodItemDTO updateFoodItem(Long id, FoodItemDTO foodItemDTO) {
        // Check if food item exists
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new FoodItemNotFoundException("Food item not found with id: " + id));
        
        // Check if category exists
        Category category = null;
        if (foodItemDTO.getCategoryId() != null) {
            category = categoryRepository.findById(foodItemDTO.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + foodItemDTO.getCategoryId()));
        }
        
        // Update food item
        foodItem.setName(foodItemDTO.getName());
        foodItem.setDescription(foodItemDTO.getDescription());
        foodItem.setPrice(foodItemDTO.getPrice());
        foodItem.setAvailable(foodItemDTO.isAvailable());
        foodItem.setCategory(category);
        
        // Save food item
        FoodItem updatedFoodItem = foodItemRepository.save(foodItem);
        
        // Return DTO
        return mapToDTO(updatedFoodItem);
    }
    
    @Override
    public FoodItemDTO updateFoodItemAvailability(Long id, boolean available) {
        // Check if food item exists
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new FoodItemNotFoundException("Food item not found with id: " + id));
        
        // Update availability
        foodItem.setAvailable(available);
        
        // Save food item
        FoodItem updatedFoodItem = foodItemRepository.save(foodItem);
        
        // Return DTO
        return mapToDTO(updatedFoodItem);
    }
    
    @Override
    public void deleteFoodItem(Long id) {
        // Check if food item exists
        if (!foodItemRepository.existsById(id)) {
            throw new FoodItemNotFoundException("Food item not found with id: " + id);
        }
        
        // Delete food item
        foodItemRepository.deleteById(id);
    }
    
    private FoodItemDTO mapToDTO(FoodItem foodItem) {
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(foodItem.getId());
        dto.setName(foodItem.getName());
        dto.setDescription(foodItem.getDescription());
        dto.setPrice(foodItem.getPrice());
        dto.setAvailable(foodItem.isAvailable());
        dto.setCreatedAt(foodItem.getCreatedAt());
        
        if (foodItem.getCategory() != null) {
            dto.setCategoryId(foodItem.getCategory().getId());
            dto.setCategoryName(foodItem.getCategory().getName());
        }
        
        return dto;
    }
}
