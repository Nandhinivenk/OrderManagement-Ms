package com.cloudkitchen.model;

import java.time.LocalDateTime;

/**
 * Model class for InventoryItem
 */
public class InventoryItem {
    private int id;
    private String name;
    private int quantity;
    private String unit;
    private int reorderLevel;
    private String qrCodePath;
    private LocalDateTime lastUpdated;
    
    // Default constructor
    public InventoryItem() {
    }
    
    // Constructor with required fields
    public InventoryItem(String name, int quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Constructor with all fields
    public InventoryItem(int id, String name, int quantity, String unit, 
                        int reorderLevel, String qrCodePath, LocalDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.reorderLevel = reorderLevel;
        this.qrCodePath = qrCodePath;
        this.lastUpdated = lastUpdated;
    }
    
    // Check if item needs to be reordered
    public boolean needsReorder() {
        return quantity <= reorderLevel;
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
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public int getReorderLevel() {
        return reorderLevel;
    }
    
    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
    
    public String getQrCodePath() {
        return qrCodePath;
    }
    
    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    @Override
    public String toString() {
        return "InventoryItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", reorderLevel=" + reorderLevel +
                ", qrCodePath='" + qrCodePath + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
