package com.cloudkitchen.customerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
}
