package com.example.erp.repository;

import com.example.erp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);
    Employee findByCode(String code);
    boolean existsByEmail(String email);
    boolean existsByCode(String code);
}
