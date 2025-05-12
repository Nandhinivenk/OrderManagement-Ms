package com.cloudkitchen.deliveryservice.exception;

public class DeliveryAlreadyExistsException extends RuntimeException {
    
    public DeliveryAlreadyExistsException(String message) {
        super(message);
    }
}
