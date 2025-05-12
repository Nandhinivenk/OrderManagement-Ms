package com.cloudkitchen.kitchenflowservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_flows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFlow {
    
    @Id
    @Column(name = "order_id")
    private Long orderId;
    
    @Column(nullable = false)
    private String currentStatus;
    
    private Long customerId;
    
    private String customerName;
    
    private LocalDateTime orderDate;
    
    private String deliveryPerson;
    
    private LocalDateTime estimatedDeliveryTime;
    
    @OneToMany(mappedBy = "orderFlow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatusUpdate> statusUpdates = new ArrayList<>();
    
    // Order status constants
    public static final String STATUS_RECEIVED = "RECEIVED";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_PREPARING = "PREPARING";
    public static final String STATUS_READY = "READY";
    public static final String STATUS_ASSIGNED = "ASSIGNED";
    public static final String STATUS_IN_TRANSIT = "IN_TRANSIT";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    
    public void addStatusUpdate(String status, String notes) {
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setOrderFlow(this);
        statusUpdate.setStatus(status);
        statusUpdate.setNotes(notes);
        statusUpdate.setUpdateTime(LocalDateTime.now());
        
        this.statusUpdates.add(statusUpdate);
        this.currentStatus = status;
    }
}
