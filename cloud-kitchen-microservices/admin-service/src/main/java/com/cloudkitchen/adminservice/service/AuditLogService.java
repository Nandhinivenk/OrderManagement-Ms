package com.cloudkitchen.adminservice.service;

import com.cloudkitchen.adminservice.dto.AuditLogDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogService {
    
    void logAction(String adminUsername, String action, String entityType, Long entityId, String details);
    
    AuditLogDTO getAuditLogById(Long id);
    
    List<AuditLogDTO> getAuditLogsByAdmin(String adminUsername);
    
    List<AuditLogDTO> getAuditLogsByEntity(String entityType, Long entityId);
    
    List<AuditLogDTO> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<AuditLogDTO> getAllAuditLogs();
}
