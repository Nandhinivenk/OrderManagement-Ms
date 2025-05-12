package com.cloudkitchen.kitchenflowservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFlowDTO {
    
    private Long orderId;
    private String currentStatus;
    private Long customerId;
    private String customerName;
    private LocalDateTime orderDate;
    private String deliveryPerson;
    private LocalDateTime estimatedDeliveryTime;
    private List<StatusUpdateDTO> statusUpdates = new ArrayList<>();
}
