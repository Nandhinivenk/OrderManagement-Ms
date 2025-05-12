package com.cloudkitchen.orderservice.exception;

public class FoodItemNotFoundException extends RuntimeException {
    
    public FoodItemNotFoundException(String message) {
        super(message);
    }
}
