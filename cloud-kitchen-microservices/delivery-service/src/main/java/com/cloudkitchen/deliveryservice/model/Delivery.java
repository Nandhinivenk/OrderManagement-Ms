package com.cloudkitchen.deliveryservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private Long orderId;
    
    private String deliveryPerson;
    
    @Column(nullable = false)
    private String deliveryStatus;
    
    private LocalDateTime deliveryTime;
    
    @Column(nullable = false)
    private String deliveryAddress;
    
    // Delivery status constants
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_ASSIGNED = "ASSIGNED";
    public static final String STATUS_IN_TRANSIT = "IN_TRANSIT";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_FAILED = "FAILED";
    
    @PrePersist
    public void prePersist() {
        if (deliveryStatus == null) {
            deliveryStatus = STATUS_PENDING;
        }
    }
}
