package com.cloudkitchen.adminservice.controller;

import com.cloudkitchen.adminservice.dto.AuditLogDTO;
import com.cloudkitchen.adminservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
    
    private final AuditLogService auditLogService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> getAllAuditLogs() {
        List<AuditLogDTO> auditLogs = auditLogService.getAllAuditLogs();
        return ResponseEntity.ok(auditLogs);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuditLogDTO> getAuditLogById(@PathVariable Long id) {
        AuditLogDTO auditLog = auditLogService.getAuditLogById(id);
        return ResponseEntity.ok(auditLog);
    }
    
    @GetMapping("/admin/{adminUsername}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByAdmin(@PathVariable String adminUsername) {
        List<AuditLogDTO> auditLogs = auditLogService.getAuditLogsByAdmin(adminUsername);
        return ResponseEntity.ok(auditLogs);
    }
    
    @GetMapping("/entity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByEntity(
            @RequestParam String entityType,
            @RequestParam Long entityId) {
        List<AuditLogDTO> auditLogs = auditLogService.getAuditLogsByEntity(entityType, entityId);
        return ResponseEntity.ok(auditLogs);
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AuditLogDTO> auditLogs = auditLogService.getAuditLogsByDateRange(startDate, endDate);
        return ResponseEntity.ok(auditLogs);
    }
}
