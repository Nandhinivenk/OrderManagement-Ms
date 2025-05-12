package com.cloudkitchen.adminservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String adminUsername;
    
    @Column(nullable = false)
    private String action;
    
    @Column(nullable = false)
    private String entityType;
    
    private Long entityId;
    
    @Column(length = 1000)
    private String details;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @PrePersist
    public void prePersist() {
        timestamp = LocalDateTime.now();
    }
}
