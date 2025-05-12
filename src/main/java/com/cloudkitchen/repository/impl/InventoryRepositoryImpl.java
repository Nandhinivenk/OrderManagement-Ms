package com.cloudkitchen.repository.impl;

import com.cloudkitchen.model.InventoryItem;
import com.cloudkitchen.repository.InventoryRepository;
import com.cloudkitchen.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of InventoryRepository
 */
public class InventoryRepositoryImpl implements InventoryRepository {
    
    @Override
    public InventoryItem save(InventoryItem item) {
        String sql = "INSERT INTO inventory_items (name, quantity, unit, reorder_level, qr_code_path) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setString(3, item.getUnit());
            stmt.setInt(4, item.getReorderLevel());
            stmt.setString(5, item.getQrCodePath());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating inventory item failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating inventory item failed, no ID obtained.");
                }
            }
            
            return item;
        } catch (SQLException e) {
            System.err.println("Error saving inventory item: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Optional<InventoryItem> findById(int id) {
        String sql = "SELECT * FROM inventory_items WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    InventoryItem item = mapResultSetToInventoryItem(rs);
                    return Optional.of(item);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding inventory item by ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<InventoryItem> findByName(String name) {
        String sql = "SELECT * FROM inventory_items WHERE name = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    InventoryItem item = mapResultSetToInventoryItem(rs);
                    return Optional.of(item);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding inventory item by name: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Override
    public List<InventoryItem> findAll() {
        String sql = "SELECT * FROM inventory_items";
        List<InventoryItem> items = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                InventoryItem item = mapResultSetToInventoryItem(rs);
                items.add(item);
            }
            
            return items;
        } catch (SQLException e) {
            System.err.println("Error finding all inventory items: " + e.getMessage());
            e.printStackTrace();
            return items;
        }
    }
    
    @Override
    public List<InventoryItem> findItemsToReorder() {
        String sql = "SELECT * FROM inventory_items WHERE quantity <= reorder_level";
        List<InventoryItem> items = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                InventoryItem item = mapResultSetToInventoryItem(rs);
                items.add(item);
            }
            
            return items;
        } catch (SQLException e) {
            System.err.println("Error finding inventory items to reorder: " + e.getMessage());
            e.printStackTrace();
            return items;
        }
    }
    
    @Override
    public InventoryItem update(InventoryItem item) {
        String sql = "UPDATE inventory_items SET name = ?, quantity = ?, unit = ?, " +
                     "reorder_level = ?, qr_code_path = ?, last_updated = ? WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, item.getName());
            stmt.setInt(2, item.getQuantity());
            stmt.setString(3, item.getUnit());
            stmt.setInt(4, item.getReorderLevel());
            stmt.setString(5, item.getQrCodePath());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(7, item.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating inventory item failed, no rows affected.");
            }
            
            return item;
        } catch (SQLException e) {
            System.err.println("Error updating inventory item: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM inventory_items WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting inventory item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Map a ResultSet to an InventoryItem object
     * 
     * @param rs The ResultSet to map
     * @return The mapped InventoryItem object
     * @throws SQLException If an error occurs while mapping
     */
    private InventoryItem mapResultSetToInventoryItem(ResultSet rs) throws SQLException {
        InventoryItem item = new InventoryItem();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnit(rs.getString("unit"));
        item.setReorderLevel(rs.getInt("reorder_level"));
        item.setQrCodePath(rs.getString("qr_code_path"));
        
        Timestamp lastUpdated = rs.getTimestamp("last_updated");
        if (lastUpdated != null) {
            item.setLastUpdated(lastUpdated.toLocalDateTime());
        } else {
            item.setLastUpdated(LocalDateTime.now());
        }
        
        return item;
    }
}
