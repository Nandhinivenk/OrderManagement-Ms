package com.cloudkitchen.adminservice.exception;

public class AuditLogNotFoundException extends RuntimeException {
    
    public AuditLogNotFoundException(String message) {
        super(message);
    }
}
