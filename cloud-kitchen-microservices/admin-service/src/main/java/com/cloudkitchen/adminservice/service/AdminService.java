package com.cloudkitchen.adminservice.service;

import com.cloudkitchen.adminservice.dto.AdminDTO;
import com.cloudkitchen.adminservice.dto.AdminRegistrationRequest;
import com.cloudkitchen.adminservice.dto.LoginRequest;
import com.cloudkitchen.adminservice.dto.LoginResponse;

import java.util.List;

public interface AdminService {
    
    AdminDTO registerAdmin(AdminRegistrationRequest registrationRequest);
    
    LoginResponse authenticateAdmin(LoginRequest loginRequest);
    
    AdminDTO getAdminById(Long id);
    
    AdminDTO getAdminByUsername(String username);
    
    List<AdminDTO> getAllAdmins();
    
    AdminDTO updateAdmin(Long id, AdminDTO adminDTO);
    
    void deleteAdmin(Long id);
}
