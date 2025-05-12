package com.cloudkitchen.model;

import java.math.BigDecimal;

/**
 * Model class for OrderItem
 */
public class OrderItem {
    private int id;
    private int orderId;
    private int foodItemId;
    private int quantity;
    private BigDecimal price;
    private FoodItem foodItem; // For reference
    
    // Default constructor
    public OrderItem() {
    }
    
    // Constructor with required fields
    public OrderItem(int foodItemId, int quantity, BigDecimal price) {
        this.foodItemId = foodItemId;
        this.quantity = quantity;
        this.price = price;
    }
    
    // Constructor with all fields
    public OrderItem(int id, int orderId, int foodItemId, int quantity, BigDecimal price) {
        this.id = id;
        this.orderId = orderId;
        this.foodItemId = foodItemId;
        this.quantity = quantity;
        this.price = price;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getFoodItemId() {
        return foodItemId;
    }
    
    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public FoodItem getFoodItem() {
        return foodItem;
    }
    
    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }
    
    // Calculate the subtotal for this item
    public BigDecimal getSubtotal() {
        return price.multiply(new BigDecimal(quantity));
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", foodItemId=" + foodItemId +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
