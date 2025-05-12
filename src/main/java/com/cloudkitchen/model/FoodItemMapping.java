package com.cloudkitchen.model;

/**
 * Model class for FoodItemMapping
 */
public class FoodItemMapping {
    private int id;
    private int foodItemId;
    private int categoryId;
    
    // Default constructor
    public FoodItemMapping() {
    }
    
    // Constructor with required fields
    public FoodItemMapping(int foodItemId, int categoryId) {
        this.foodItemId = foodItemId;
        this.categoryId = categoryId;
    }
    
    // Constructor with all fields
    public FoodItemMapping(int id, int foodItemId, int categoryId) {
        this.id = id;
        this.foodItemId = foodItemId;
        this.categoryId = categoryId;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getFoodItemId() {
        return foodItemId;
    }
    
    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    @Override
    public String toString() {
        return "FoodItemMapping{" +
                "id=" + id +
                ", foodItemId=" + foodItemId +
                ", categoryId=" + categoryId +
                '}';
    }
}
