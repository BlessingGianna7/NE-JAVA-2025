package com.example.erp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "employments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String position;

    @Column(name = "base_salary", nullable = false)
    private BigDecimal baseSalary;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus status = EmploymentStatus.ACTIVE;

    @Column(name = "joining_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date joiningDate;

    public enum EmploymentStatus {
        ACTIVE, INACTIVE
    }
}