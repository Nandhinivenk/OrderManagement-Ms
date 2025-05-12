package com.cloudkitchen.foodcatalogservice.service.impl;

import com.cloudkitchen.foodcatalogservice.dto.CategoryDTO;
import com.cloudkitchen.foodcatalogservice.exception.CategoryNotFoundException;
import com.cloudkitchen.foodcatalogservice.model.Category;
import com.cloudkitchen.foodcatalogservice.repository.CategoryRepository;
import com.cloudkitchen.foodcatalogservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        // Check if category name already exists
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name " + categoryDTO.getName() + " already exists");
        }
        
        // Create new category
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        
        // Save category
        Category savedCategory = categoryRepository.save(category);
        
        // Return DTO
        return mapToDTO(savedCategory);
    }
    
    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        
        return mapToDTO(category);
    }
    
    @Override
    public CategoryDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with name: " + name));
        
        return mapToDTO(category);
    }
    
    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        // Check if category exists
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        
        // Check if new name already exists (if name is being changed)
        if (!category.getName().equals(categoryDTO.getName()) && 
                categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name " + categoryDTO.getName() + " already exists");
        }
        
        // Update category
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        
        // Save category
        Category updatedCategory = categoryRepository.save(category);
        
        // Return DTO
        return mapToDTO(updatedCategory);
    }
    
    @Override
    public void deleteCategory(Long id) {
        // Check if category exists
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }
        
        // Delete category
        categoryRepository.deleteById(id);
    }
    
    private CategoryDTO mapToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
}
