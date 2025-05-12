package com.cloudkitchen.model;

import java.time.LocalDateTime;

/**
 * Model class for Delivery
 */
public class Delivery {
    private int id;
    private int orderId;
    private String deliveryPerson;
    private String deliveryStatus;
    private LocalDateTime deliveryTime;
    private String deliveryAddress;
    
    // Delivery status constants
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_ASSIGNED = "ASSIGNED";
    public static final String STATUS_IN_TRANSIT = "IN_TRANSIT";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_FAILED = "FAILED";
    
    // Default constructor
    public Delivery() {
    }
    
    // Constructor with required fields
    public Delivery(int orderId, String deliveryAddress) {
        this.orderId = orderId;
        this.deliveryAddress = deliveryAddress;
        this.deliveryStatus = STATUS_PENDING;
    }
    
    // Constructor with all fields
    public Delivery(int id, int orderId, String deliveryPerson, String deliveryStatus, 
                   LocalDateTime deliveryTime, String deliveryAddress) {
        this.id = id;
        this.orderId = orderId;
        this.deliveryPerson = deliveryPerson;
        this.deliveryStatus = deliveryStatus;
        this.deliveryTime = deliveryTime;
        this.deliveryAddress = deliveryAddress;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public String getDeliveryPerson() {
        return deliveryPerson;
    }
    
    public void setDeliveryPerson(String deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }
    
    public String getDeliveryStatus() {
        return deliveryStatus;
    }
    
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
    
    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }
    
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", deliveryPerson='" + deliveryPerson + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", deliveryTime=" + deliveryTime +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                '}';
    }
}
