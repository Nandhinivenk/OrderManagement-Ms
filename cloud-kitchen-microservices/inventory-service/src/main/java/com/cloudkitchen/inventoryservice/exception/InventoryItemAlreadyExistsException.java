package com.cloudkitchen.inventoryservice.exception;

public class InventoryItemAlreadyExistsException extends RuntimeException {
    
    public InventoryItemAlreadyExistsException(String message) {
        super(message);
    }
}
