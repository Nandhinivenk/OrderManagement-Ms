package com.cloudkitchen.adminservice.service.impl;

import com.cloudkitchen.adminservice.dto.AuditLogDTO;
import com.cloudkitchen.adminservice.exception.AuditLogNotFoundException;
import com.cloudkitchen.adminservice.model.AuditLog;
import com.cloudkitchen.adminservice.repository.AuditLogRepository;
import com.cloudkitchen.adminservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    
    private final AuditLogRepository auditLogRepository;
    
    @Override
    @Transactional
    public void logAction(String adminUsername, String action, String entityType, Long entityId, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAdminUsername(adminUsername);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setDetails(details);
        auditLog.setTimestamp(LocalDateTime.now());
        
        auditLogRepository.save(auditLog);
    }
    
    @Override
    public AuditLogDTO getAuditLogById(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new AuditLogNotFoundException("Audit log not found with id: " + id));
        
        return mapToDTO(auditLog);
    }
    
    @Override
    public List<AuditLogDTO> getAuditLogsByAdmin(String adminUsername) {
        return auditLogRepository.findByAdminUsername(adminUsername).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AuditLogDTO> getAuditLogsByEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AuditLogDTO> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AuditLogDTO> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private AuditLogDTO mapToDTO(AuditLog auditLog) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(auditLog.getId());
        dto.setAdminUsername(auditLog.getAdminUsername());
        dto.setAction(auditLog.getAction());
        dto.setEntityType(auditLog.getEntityType());
        dto.setEntityId(auditLog.getEntityId());
        dto.setDetails(auditLog.getDetails());
        dto.setTimestamp(auditLog.getTimestamp());
        return dto;
    }
}
