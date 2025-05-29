package com.example.erp.service;

import com.example.erp.model.Employee;
import com.example.erp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(UUID id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        return employee;
    }

    public Employee getEmployeeByCode(String code) {
        Employee employee = employeeRepository.findByCode(code);
        if (employee == null) {
            throw new RuntimeException("Employee not found with code: " + code);
        }
        return employee;
    }

    public Employee getCurrentEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Not authenticated");
        }
        
        String email = authentication.getName();
        Employee employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            throw new RuntimeException("Current employee not found");
        }
        
        return employee;
    }

    public Employee updateEmployee(UUID id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id);
        
        // Only update non-null fields
        if (employeeDetails.getFirstName() != null) {
            employee.setFirstName(employeeDetails.getFirstName());
        }
        if (employeeDetails.getLastName() != null) {
            employee.setLastName(employeeDetails.getLastName());
        }
        if (employeeDetails.getMobile() != null) {
            employee.setMobile(employeeDetails.getMobile());
        }
        if (employeeDetails.getDateOfBirth() != null) {
            employee.setDateOfBirth(employeeDetails.getDateOfBirth());
        }
        if (employeeDetails.getStatus() != null) {
            employee.setStatus(employeeDetails.getStatus());
        }
        
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(UUID id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }
}