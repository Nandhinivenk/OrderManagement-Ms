package com.cloudkitchen.kitchenflowservice.exception;

public class InvalidStatusTransitionException extends RuntimeException {
    
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
