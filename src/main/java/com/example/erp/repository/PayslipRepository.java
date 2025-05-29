package com.example.erp.repository;

import com.example.erp.model.Employee;
import com.example.erp.model.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, UUID> {
    List<Payslip> findByEmployee(Employee employee);
    List<Payslip> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
    List<Payslip> findByMonthAndYear(Integer month, Integer year);
    List<Payslip> findByStatus(Payslip.PayslipStatus status);
    boolean existsByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
}