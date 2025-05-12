package com.cloudkitchen.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
    
    private Long id;
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    private String deliveryPerson;
    
    private String deliveryStatus;
    
    private LocalDateTime deliveryTime;
    
    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;
    
    // Order information
    private String orderStatus;
    private String customerName;
}
