package com.cloudkitchen.inventoryservice.exception;

public class InventoryItemNotFoundException extends RuntimeException {
    
    public InventoryItemNotFoundException(String message) {
        super(message);
    }
}
