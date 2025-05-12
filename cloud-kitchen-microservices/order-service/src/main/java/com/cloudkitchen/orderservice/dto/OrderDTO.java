package com.cloudkitchen.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    
    private Long id;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String customerName;
    
    private LocalDateTime orderDate;
    
    private String status;
    
    private BigDecimal totalAmount;
    
    private String paymentMethod;
    
    private String paymentStatus;
    
    @Valid
    private List<OrderItemDTO> orderItems = new ArrayList<>();
}
