package com.cloudkitchen.kitchenflowservice.exception;

public class OrderFlowNotFoundException extends RuntimeException {
    
    public OrderFlowNotFoundException(String message) {
        super(message);
    }
}
