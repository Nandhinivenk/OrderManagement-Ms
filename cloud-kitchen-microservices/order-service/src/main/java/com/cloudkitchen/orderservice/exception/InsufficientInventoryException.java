package com.cloudkitchen.orderservice.exception;

public class InsufficientInventoryException extends RuntimeException {
    
    public InsufficientInventoryException(String message) {
        super(message);
    }
}
