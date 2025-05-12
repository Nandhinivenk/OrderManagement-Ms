package com.cloudkitchen.foodcatalogservice.service;

import com.cloudkitchen.foodcatalogservice.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    
    CategoryDTO getCategoryById(Long id);
    
    CategoryDTO getCategoryByName(String name);
    
    List<CategoryDTO> getAllCategories();
    
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    
    void deleteCategory(Long id);
}
