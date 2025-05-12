package com.cloudkitchen.kitchenflowservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "status_updates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String status;
    
    private String notes;
    
    @Column(nullable = false)
    private LocalDateTime updateTime;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private OrderFlow orderFlow;
    
    @PrePersist
    public void prePersist() {
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
    }
}
