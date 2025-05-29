package com.example.erp.repository;

import com.example.erp.model.Employee;
import com.example.erp.model.Employment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, UUID> {
    Employment findByCode(String code);
    List<Employment> findByEmployee(Employee employee);
    List<Employment> findByEmployeeAndStatus(Employee employee, Employment.EmploymentStatus status);
    boolean existsByCode(String code);
}