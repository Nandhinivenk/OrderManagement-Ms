package com.cloudkitchen.adminservice.repository;

import com.cloudkitchen.adminservice.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    List<AuditLog> findByAdminUsername(String adminUsername);
    
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
    
    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
