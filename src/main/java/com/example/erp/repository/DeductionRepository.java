package com.example.erp.repository;

import com.example.erp.model.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeductionRepository extends JpaRepository<Deduction, Long> {
    Deduction findByCode(String code);
    Deduction findByDeductionName(String deductionName);
    boolean existsByCode(String code);
    boolean existsByDeductionName(String deductionName);
}