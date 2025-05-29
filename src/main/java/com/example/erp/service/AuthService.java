package com.example.erp.service;

import com.example.erp.dto.AuthRequest;
import com.example.erp.dto.AuthResponse;
import com.example.erp.dto.RegisterRequest;
import com.example.erp.model.Employee;
import com.example.erp.repository.EmployeeRepository;
import com.example.erp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        Employee employee = (Employee) authentication.getPrincipal();
        String token = jwtUtil.generateToken(employee);
        
        return new AuthResponse(token, employee.getEmail(), employee.getFirstName(), 
                employee.getLastName(), employee.getRoles());
    }

    public AuthResponse register(RegisterRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        Employee employee = new Employee();
        employee.setCode(generateEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setMobile(request.getMobile());
        employee.setDateOfBirth(request.getDateOfBirth());
        
        List<String> roles = request.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = new ArrayList<String>();
            roles.add("ROLE_EMPLOYEE");
        }
        employee.setRoles(roles);
        
        employee = employeeRepository.save(employee);
        
        String token = jwtUtil.generateToken(employee);
        
        return new AuthResponse(token, employee.getEmail(), employee.getFirstName(), 
                employee.getLastName(), employee.getRoles());
    }
    
    private String generateEmployeeCode() {
        return "EMP" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}