package com.cloudkitchen.foodcatalogservice.exception;

public class FoodItemNotFoundException extends RuntimeException {
    
    public FoodItemNotFoundException(String message) {
        super(message);
    }
}
