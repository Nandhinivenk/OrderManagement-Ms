package com.cloudkitchen.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
    
    private Long foodItemId;
    
    private String foodItemName;
    
    private int quantity;
    
    private BigDecimal price;
    
    // Helper method to calculate the subtotal
    public BigDecimal getSubtotal() {
        return price.multiply(new BigDecimal(quantity));
    }
}
