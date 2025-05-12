package com.cloudkitchen.repository.impl;

import com.cloudkitchen.model.FoodItem;
import com.cloudkitchen.model.OrderItem;
import com.cloudkitchen.repository.FoodItemRepository;
import com.cloudkitchen.repository.OrderItemRepository;
import com.cloudkitchen.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of OrderItemRepository
 */
public class OrderItemRepositoryImpl implements OrderItemRepository {
    
    private final FoodItemRepository foodItemRepository;
    
    public OrderItemRepositoryImpl() {
        this.foodItemRepository = new FoodItemRepositoryImpl();
    }
    
    @Override
    public OrderItem save(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, food_item_id, quantity, price) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getFoodItemId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setBigDecimal(4, orderItem.getPrice());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating order item failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderItem.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating order item failed, no ID obtained.");
                }
            }
            
            return orderItem;
        } catch (SQLException e) {
            System.err.println("Error saving order item: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Optional<OrderItem> findById(int id) {
        String sql = "SELECT * FROM order_items WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrderItem orderItem = mapResultSetToOrderItem(rs);
                    
                    // Load the food item
                    Optional<FoodItem> foodItemOpt = foodItemRepository.findById(orderItem.getFoodItemId());
                    foodItemOpt.ifPresent(orderItem::setFoodItem);
                    
                    return Optional.of(orderItem);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding order item by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Override
    public List<OrderItem> findByOrderId(int orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        List<OrderItem> orderItems = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem orderItem = mapResultSetToOrderItem(rs);
                    
                    // Load the food item
                    Optional<FoodItem> foodItemOpt = foodItemRepository.findById(orderItem.getFoodItemId());
                    foodItemOpt.ifPresent(orderItem::setFoodItem);
                    
                    orderItems.add(orderItem);
                }
            }
            
            return orderItems;
        } catch (SQLException e) {
            System.err.println("Error finding order items by order ID: " + e.getMessage());
            e.printStackTrace();
            return orderItems;
        }
    }
    
    @Override
    public OrderItem update(OrderItem orderItem) {
        String sql = "UPDATE order_items SET order_id = ?, food_item_id = ?, quantity = ?, price = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getFoodItemId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setBigDecimal(4, orderItem.getPrice());
            stmt.setInt(5, orderItem.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating order item failed, no rows affected.");
            }
            
            return orderItem;
        } catch (SQLException e) {
            System.err.println("Error updating order item: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM order_items WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting order item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteByOrderId(int orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting order items by order ID: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Map a ResultSet to an OrderItem object
     * 
     * @param rs The ResultSet to map
     * @return The mapped OrderItem object
     * @throws SQLException If an error occurs while mapping
     */
    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getInt("id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setFoodItemId(rs.getInt("food_item_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setPrice(rs.getBigDecimal("price"));
        
        return orderItem;
    }
}
