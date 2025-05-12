package com.cloudkitchen.repository.impl;

import com.cloudkitchen.model.Delivery;
import com.cloudkitchen.repository.DeliveryRepository;
import com.cloudkitchen.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of DeliveryRepository
 */
public class DeliveryRepositoryImpl implements DeliveryRepository {
    
    @Override
    public Delivery save(Delivery delivery) {
        String sql = "INSERT INTO deliveries (order_id, delivery_person, delivery_status, delivery_time, delivery_address) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, delivery.getOrderId());
            stmt.setString(2, delivery.getDeliveryPerson());
            stmt.setString(3, delivery.getDeliveryStatus());
            
            if (delivery.getDeliveryTime() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(delivery.getDeliveryTime()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }
            
            stmt.setString(5, delivery.getDeliveryAddress());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating delivery failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    delivery.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating delivery failed, no ID obtained.");
                }
            }
            
            return delivery;
        } catch (SQLException e) {
            System.err.println("Error saving delivery: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Optional<Delivery> findById(int id) {
        String sql = "SELECT * FROM deliveries WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Delivery delivery = mapResultSetToDelivery(rs);
                    return Optional.of(delivery);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding delivery by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Delivery> findByOrderId(int orderId) {
        String sql = "SELECT * FROM deliveries WHERE order_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Delivery delivery = mapResultSetToDelivery(rs);
                    return Optional.of(delivery);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding delivery by order ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Override
    public List<Delivery> findAll() {
        String sql = "SELECT * FROM deliveries";
        List<Delivery> deliveries = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Delivery delivery = mapResultSetToDelivery(rs);
                deliveries.add(delivery);
            }
            
            return deliveries;
        } catch (SQLException e) {
            System.err.println("Error finding all deliveries: " + e.getMessage());
            e.printStackTrace();
            return deliveries;
        }
    }
    
    @Override
    public List<Delivery> findByStatus(String status) {
        String sql = "SELECT * FROM deliveries WHERE delivery_status = ?";
        List<Delivery> deliveries = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Delivery delivery = mapResultSetToDelivery(rs);
                    deliveries.add(delivery);
                }
            }
            
            return deliveries;
        } catch (SQLException e) {
            System.err.println("Error finding deliveries by status: " + e.getMessage());
            e.printStackTrace();
            return deliveries;
        }
    }
    
    @Override
    public List<Delivery> findByDeliveryPerson(String deliveryPerson) {
        String sql = "SELECT * FROM deliveries WHERE delivery_person = ?";
        List<Delivery> deliveries = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, deliveryPerson);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Delivery delivery = mapResultSetToDelivery(rs);
                    deliveries.add(delivery);
                }
            }
            
            return deliveries;
        } catch (SQLException e) {
            System.err.println("Error finding deliveries by delivery person: " + e.getMessage());
            e.printStackTrace();
            return deliveries;
        }
    }
    
    @Override
    public Delivery update(Delivery delivery) {
        String sql = "UPDATE deliveries SET order_id = ?, delivery_person = ?, delivery_status = ?, " +
                     "delivery_time = ?, delivery_address = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, delivery.getOrderId());
            stmt.setString(2, delivery.getDeliveryPerson());
            stmt.setString(3, delivery.getDeliveryStatus());
            
            if (delivery.getDeliveryTime() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(delivery.getDeliveryTime()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }
            
            stmt.setString(5, delivery.getDeliveryAddress());
            stmt.setInt(6, delivery.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating delivery failed, no rows affected.");
            }
            
            return delivery;
        } catch (SQLException e) {
            System.err.println("Error updating delivery: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM deliveries WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting delivery: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Map a ResultSet to a Delivery object
     * 
     * @param rs The ResultSet to map
     * @return The mapped Delivery object
     * @throws SQLException If an error occurs while mapping
     */
    private Delivery mapResultSetToDelivery(ResultSet rs) throws SQLException {
        Delivery delivery = new Delivery();
        delivery.setId(rs.getInt("id"));
        delivery.setOrderId(rs.getInt("order_id"));
        delivery.setDeliveryPerson(rs.getString("delivery_person"));
        delivery.setDeliveryStatus(rs.getString("delivery_status"));
        
        Timestamp deliveryTime = rs.getTimestamp("delivery_time");
        if (deliveryTime != null) {
            delivery.setDeliveryTime(deliveryTime.toLocalDateTime());
        }
        
        delivery.setDeliveryAddress(rs.getString("delivery_address"));
        
        return delivery;
    }
}
