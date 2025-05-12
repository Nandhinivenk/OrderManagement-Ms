package com.cloudkitchen.kitchenflowservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {
    
    @NotBlank(message = "Status is required")
    private String status;
    
    private String notes;
}
