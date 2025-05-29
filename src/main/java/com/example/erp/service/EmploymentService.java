package com.example.erp.service;

import com.example.erp.dto.EmploymentRequest;
import com.example.erp.model.Employee;
import com.example.erp.model.Employment;
import com.example.erp.repository.EmploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;

@Service
public class EmploymentService {

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private EmployeeService employeeService;

    public List<Employment> getAllEmployments() {
        return employmentRepository.findAll();
    }

    public Employment getEmploymentById(UUID id) {
        Employment employment = employmentRepository.findById(id).orElse(null);
        if (employment == null) {
            throw new RuntimeException("Employment not found with id: " + id);
        }
        return employment;
    }

    public Employment getEmploymentByCode(String code) {
        Employment employment = employmentRepository.findByCode(code);
        if (employment == null) {
            throw new RuntimeException("Employment not found with code: " + code);
        }
        return employment;
    }

    public List<Employment> getEmploymentsByEmployee(UUID employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return employmentRepository.findByEmployee(employee);
    }

    public List<Employment> getActiveEmploymentsByEmployee(UUID employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return employmentRepository.findByEmployeeAndStatus(employee, Employment.EmploymentStatus.ACTIVE);
    }

    public Employment createEmployment(EmploymentRequest request) {
        Employee employee = employeeService.getEmployeeByCode(request.getEmployeeCode());
        
        Employment employment = new Employment();
        employment.setCode(generateEmploymentCode());
        employment.setEmployee(employee);
        employment.setDepartment(request.getDepartment());
        employment.setPosition(request.getPosition());
        employment.setBaseSalary(request.getBaseSalary());
        employment.setJoiningDate(request.getJoiningDate());
        employment.setStatus(Employment.EmploymentStatus.ACTIVE);
        
        return employmentRepository.save(employment);
    }

    public Employment updateEmployment(UUID id, EmploymentRequest request) {
        Employment employment = getEmploymentById(id);
        
        if (request.getDepartment() != null) {
            employment.setDepartment(request.getDepartment());
        }
        if (request.getPosition() != null) {
            employment.setPosition(request.getPosition());
        }
        if (request.getBaseSalary() != null) {
            employment.setBaseSalary(request.getBaseSalary());
        }
        if (request.getJoiningDate() != null) {
            employment.setJoiningDate(request.getJoiningDate());
        }
        
        return employmentRepository.save(employment);
    }

    public Employment activateEmployment(UUID id) {
        Employment employment = getEmploymentById(id);
        employment.setStatus(Employment.EmploymentStatus.ACTIVE);
        return employmentRepository.save(employment);
    }

    public Employment deactivateEmployment(UUID id) {
        Employment employment = getEmploymentById(id);
        employment.setStatus(Employment.EmploymentStatus.INACTIVE);
        return employmentRepository.save(employment);
    }

    private String generateEmploymentCode() {
        return "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}