package com.cloudkitchen.inventoryservice.exception;

public class QRCodeGenerationException extends RuntimeException {
    
    public QRCodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
