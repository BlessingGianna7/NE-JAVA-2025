package com.example.erp.service;

import com.example.erp.model.Employee;
import com.example.erp.model.Employment;
import com.example.erp.model.Payslip;
import com.example.erp.model.Deduction;
import com.example.erp.repository.EmployeeRepository;
import com.example.erp.repository.EmploymentRepository;
import com.example.erp.repository.PayslipRepository;
import com.example.erp.repository.DeductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PayslipService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmploymentRepository employmentRepository;
    @Autowired
    private PayslipRepository payslipRepository;
    @Autowired
    private DeductionRepository deductionRepository;

    public void generatePayroll(int month, int year) {
        List<Employment> employments = employmentRepository.findAll();
        List<Deduction> deductions = deductionRepository.findAll();
        for (Employment employment : employments) {
            if (employment.getStatus() != Employment.EmploymentStatus.ACTIVE) continue;
            Employee employee = employment.getEmployee();
            if (employee.getStatus() != Employee.EmployeeStatus.ACTIVE) continue;
            if (payslipRepository.existsByEmployeeAndMonthAndYear(employee, month, year)) continue;
            BigDecimal baseSalary = employment.getBaseSalary();
            BigDecimal housing = baseSalary.multiply(getDeductionRate(deductions, "Housing"));
            BigDecimal transport = baseSalary.multiply(getDeductionRate(deductions, "Transport"));
            BigDecimal grossSalary = baseSalary.add(housing).add(transport);
            BigDecimal employeeTax = baseSalary.multiply(getDeductionRate(deductions, "Employee Tax"));
            BigDecimal pension = baseSalary.multiply(getDeductionRate(deductions, "Pension"));
            BigDecimal medical = baseSalary.multiply(getDeductionRate(deductions, "Medical Insurance"));
            BigDecimal others = baseSalary.multiply(getDeductionRate(deductions, "Others"));
            BigDecimal netSalary = grossSalary.subtract(employeeTax.add(pension).add(medical).add(others));
            Payslip payslip = new Payslip();
            payslip.setEmployee(employee);
            payslip.setHouseAmount(housing);
            payslip.setTransportAmount(transport);
            payslip.setGrossSalary(grossSalary);
            payslip.setEmployeeTaxedAmount(employeeTax);
            payslip.setPensionAmount(pension);
            payslip.setMedicalInsuranceAmount(medical);
            payslip.setOtherTaxedAmount(others);
            payslip.setNetSalary(netSalary);
            payslip.setMonth(month);
            payslip.setYear(year);
            payslip.setStatus(Payslip.PayslipStatus.PENDING);
            payslipRepository.save(payslip);
        }
    }

    private BigDecimal getDeductionRate(List<Deduction> deductions, String name) {
        return deductions.stream()
                .filter(d -> d.getDeductionName().equalsIgnoreCase(name))
                .findFirst()
                .map(d -> d.getPercentage().divide(BigDecimal.valueOf(100)))
                .orElse(BigDecimal.ZERO);
    }
} 