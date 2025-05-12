package com.cloudkitchen.kitchenflowservice.repository;

import com.cloudkitchen.kitchenflowservice.model.OrderFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFlowRepository extends JpaRepository<OrderFlow, Long> {
    
    List<OrderFlow> findByCurrentStatus(String status);
    
    List<OrderFlow> findByCustomerId(Long customerId);
    
    List<OrderFlow> findByDeliveryPerson(String deliveryPerson);
}
