package com.cloudkitchen.adminservice.controller;

import com.cloudkitchen.adminservice.dto.AdminDTO;
import com.cloudkitchen.adminservice.dto.AdminRegistrationRequest;
import com.cloudkitchen.adminservice.dto.LoginRequest;
import com.cloudkitchen.adminservice.dto.LoginResponse;
import com.cloudkitchen.adminservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AdminService adminService;
    
    @PostMapping("/register")
    public ResponseEntity<AdminDTO> registerAdmin(@Valid @RequestBody AdminRegistrationRequest registrationRequest) {
        AdminDTO adminDTO = adminService.registerAdmin(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(adminDTO);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = adminService.authenticateAdmin(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}
