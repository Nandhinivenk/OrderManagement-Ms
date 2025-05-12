package com.cloudkitchen.adminservice.service.impl;

import com.cloudkitchen.adminservice.dto.AdminDTO;
import com.cloudkitchen.adminservice.dto.AdminRegistrationRequest;
import com.cloudkitchen.adminservice.dto.LoginRequest;
import com.cloudkitchen.adminservice.dto.LoginResponse;
import com.cloudkitchen.adminservice.exception.AdminNotFoundException;
import com.cloudkitchen.adminservice.exception.DuplicateResourceException;
import com.cloudkitchen.adminservice.model.Admin;
import com.cloudkitchen.adminservice.repository.AdminRepository;
import com.cloudkitchen.adminservice.security.JwtTokenProvider;
import com.cloudkitchen.adminservice.service.AdminService;
import com.cloudkitchen.adminservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuditLogService auditLogService;
    
    @Override
    @Transactional
    public AdminDTO registerAdmin(AdminRegistrationRequest registrationRequest) {
        // Check if username already exists
        if (adminRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new DuplicateResourceException("Username is already taken");
        }
        
        // Check if email already exists
        if (adminRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new DuplicateResourceException("Email is already in use");
        }
        
        // Create new admin
        Admin admin = new Admin();
        admin.setUsername(registrationRequest.getUsername());
        admin.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        admin.setName(registrationRequest.getName());
        admin.setEmail(registrationRequest.getEmail());
        admin.setPhone(registrationRequest.getPhone());
        
        // Set roles
        if (registrationRequest.getRoles() != null && !registrationRequest.getRoles().isEmpty()) {
            admin.setRoles(registrationRequest.getRoles());
        } else {
            // Default role
            admin.setRoles(new HashSet<>());
            admin.getRoles().add("ADMIN");
        }
        
        Admin savedAdmin = adminRepository.save(admin);
        
        // Log the action
        auditLogService.logAction(
                savedAdmin.getUsername(),
                "REGISTER",
                "ADMIN",
                savedAdmin.getId(),
                "New admin registered: " + savedAdmin.getUsername()
        );
        
        return mapToDTO(savedAdmin);
    }
    
    @Override
    public LoginResponse authenticateAdmin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        Admin admin = adminRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with username: " + loginRequest.getUsername()));
        
        // Update last login time
        admin.setLastLogin(LocalDateTime.now());
        adminRepository.save(admin);
        
        // Log the action
        auditLogService.logAction(
                admin.getUsername(),
                "LOGIN",
                "ADMIN",
                admin.getId(),
                "Admin logged in: " + admin.getUsername()
        );
        
        return new LoginResponse(
                jwt,
                admin.getId(),
                admin.getUsername(),
                admin.getName(),
                admin.getEmail(),
                admin.getRoles()
        );
    }
    
    @Override
    public AdminDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + id));
        
        return mapToDTO(admin);
    }
    
    @Override
    public AdminDTO getAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with username: " + username));
        
        return mapToDTO(admin);
    }
    
    @Override
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public AdminDTO updateAdmin(Long id, AdminDTO adminDTO) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + id));
        
        // Check if email is being changed and is already in use
        if (!admin.getEmail().equals(adminDTO.getEmail()) && 
                adminRepository.existsByEmail(adminDTO.getEmail())) {
            throw new DuplicateResourceException("Email is already in use");
        }
        
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPhone(adminDTO.getPhone());
        
        // Update roles if provided
        if (adminDTO.getRoles() != null && !adminDTO.getRoles().isEmpty()) {
            admin.setRoles(adminDTO.getRoles());
        }
        
        Admin updatedAdmin = adminRepository.save(admin);
        
        // Log the action
        auditLogService.logAction(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                "UPDATE",
                "ADMIN",
                updatedAdmin.getId(),
                "Admin updated: " + updatedAdmin.getUsername()
        );
        
        return mapToDTO(updatedAdmin);
    }
    
    @Override
    @Transactional
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + id));
        
        adminRepository.delete(admin);
        
        // Log the action
        auditLogService.logAction(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                "DELETE",
                "ADMIN",
                id,
                "Admin deleted: " + admin.getUsername()
        );
    }
    
    private AdminDTO mapToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        dto.setName(admin.getName());
        dto.setEmail(admin.getEmail());
        dto.setPhone(admin.getPhone());
        dto.setRoles(admin.getRoles());
        dto.setCreatedAt(admin.getCreatedAt());
        dto.setLastLogin(admin.getLastLogin());
        return dto;
    }
}
