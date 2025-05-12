package com.cloudkitchen.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignDeliveryPersonRequest {
    
    @NotBlank(message = "Delivery person name is required")
    private String deliveryPerson;
}
