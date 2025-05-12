package com.cloudkitchen.util;

import com.cloudkitchen.model.Admin;
import com.cloudkitchen.model.Customer;

/**
 * Utility class for authentication
 */
public class AuthUtil {
    
    // Current logged-in user
    private static Customer currentCustomer;
    private static Admin currentAdmin;
    
    /**
     * Log in a customer
     * 
     * @param customer The customer to log in
     */
    public static void loginCustomer(Customer customer) {
        currentCustomer = customer;
        currentAdmin = null; // Ensure admin is logged out
    }
    
    /**
     * Log in an admin
     * 
     * @param admin The admin to log in
     */
    public static void loginAdmin(Admin admin) {
        currentAdmin = admin;
        currentCustomer = null; // Ensure customer is logged out
    }
    
    /**
     * Log out the current user
     */
    public static void logout() {
        currentCustomer = null;
        currentAdmin = null;
    }
    
    /**
     * Get the current logged-in customer
     * 
     * @return The current customer, or null if no customer is logged in
     */
    public static Customer getCurrentCustomer() {
        return currentCustomer;
    }
    
    /**
     * Get the current logged-in admin
     * 
     * @return The current admin, or null if no admin is logged in
     */
    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }
    
    /**
     * Check if a customer is logged in
     * 
     * @return true if a customer is logged in, false otherwise
     */
    public static boolean isCustomerLoggedIn() {
        return currentCustomer != null;
    }
    
    /**
     * Check if an admin is logged in
     * 
     * @return true if an admin is logged in, false otherwise
     */
    public static boolean isAdminLoggedIn() {
        return currentAdmin != null;
    }
    
    /**
     * Check if the current admin has a specific role
     * 
     * @param role The role to check
     * @return true if the current admin has the role, false otherwise
     */
    public static boolean hasAdminRole(String role) {
        if (!isAdminLoggedIn()) {
            return false;
        }
        
        return currentAdmin.getRole().equals(role);
    }
    
    /**
     * Check if the current admin is a super admin
     * 
     * @return true if the current admin is a super admin, false otherwise
     */
    public static boolean isSuperAdmin() {
        return hasAdminRole(Admin.ROLE_SUPER_ADMIN);
    }
}
