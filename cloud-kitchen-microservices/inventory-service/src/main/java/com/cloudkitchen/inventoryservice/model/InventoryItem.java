package com.cloudkitchen.inventoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private int quantity;
    
    @Column(nullable = false)
    private String unit;
    
    private int reorderLevel;
    
    private String qrCodePath;
    
    private LocalDateTime lastUpdated;
    
    @PrePersist
    @PreUpdate
    public void prePersist() {
        lastUpdated = LocalDateTime.now();
    }
}
