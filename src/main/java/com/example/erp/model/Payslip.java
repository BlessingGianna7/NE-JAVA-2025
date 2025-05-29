package com.example.erp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "payslips", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employee_id", "month", "year"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "house_amount", nullable = false)
    private BigDecimal houseAmount;

    @Column(name = "transport_amount", nullable = false)
    private BigDecimal transportAmount;

    @Column(name = "employee_taxed_amount", nullable = false)
    private BigDecimal employeeTaxedAmount;

    @Column(name = "pension_amount", nullable = false)
    private BigDecimal pensionAmount;

    @Column(name = "medical_insurance_amount", nullable = false)
    private BigDecimal medicalInsuranceAmount;

    @Column(name = "other_taxed_amount", nullable = false)
    private BigDecimal otherTaxedAmount;

    @Column(name = "gross_salary", nullable = false)
    private BigDecimal grossSalary;

    @Column(name = "net_salary", nullable = false)
    private BigDecimal netSalary;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    private PayslipStatus status = PayslipStatus.PENDING;

    public enum PayslipStatus {
        PENDING, PAID
    }
}