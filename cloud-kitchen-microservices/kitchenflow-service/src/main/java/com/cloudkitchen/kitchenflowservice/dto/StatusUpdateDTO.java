package com.cloudkitchen.kitchenflowservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateDTO {
    
    private Long id;
    private String status;
    private String notes;
    private LocalDateTime updateTime;
}
