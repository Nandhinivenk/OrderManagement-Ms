package com.cloudkitchen.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {
    
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;
    
    @NotBlank(message = "Unit is required")
    private String unit;
    
    @Min(value = 0, message = "Reorder level must be at least 0")
    private Integer reorderLevel;
    
    private String qrCodePath;
    
    private LocalDateTime lastUpdated;
}
