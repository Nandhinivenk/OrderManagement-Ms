package com.cloudkitchen.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class for FoodItem
 */
public class FoodItem {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private boolean isAvailable;
    private LocalDateTime createdAt;
    
    // Default constructor
    public FoodItem() {
    }
    
    // Constructor with required fields
    public FoodItem(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        this.isAvailable = true;
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with all fields
    public FoodItem(int id, String name, String description, BigDecimal price, 
                   String category, boolean isAvailable, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "FoodItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", isAvailable=" + isAvailable +
                ", createdAt=" + createdAt +
                '}';
    }
}
