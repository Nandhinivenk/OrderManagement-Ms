package com.cloudkitchen.kitchenflowservice.repository;

import com.cloudkitchen.kitchenflowservice.model.StatusUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusUpdateRepository extends JpaRepository<StatusUpdate, Long> {
    
    List<StatusUpdate> findByOrderFlowOrderIdOrderByUpdateTimeDesc(Long orderId);
}
