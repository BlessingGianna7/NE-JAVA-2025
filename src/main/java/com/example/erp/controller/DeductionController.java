package com.example.erp.controller;

import com.example.erp.model.Deduction;
import com.example.erp.repository.DeductionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/deductions")
@Tag(name = "Deduction Management", description = "Deduction Management API")
@SecurityRequirement(name = "bearerAuth")
public class DeductionController {
    @Autowired
    private DeductionRepository deductionRepository;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Get all deductions", description = "Get a list of all deductions")
    public ResponseEntity<List<Deduction>> getAllDeductions() {
        return ResponseEntity.ok(deductionRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Get deduction by ID", description = "Get a deduction by ID")
    public ResponseEntity<Deduction> getDeductionById(@PathVariable Long id) {
        return ResponseEntity.ok(deductionRepository.findById(id).orElseThrow());
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Create deduction", description = "Create a new deduction")
    public ResponseEntity<Deduction> createDeduction(@RequestBody Deduction deduction) {
        return ResponseEntity.ok(deductionRepository.save(deduction));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Update deduction", description = "Update a deduction")
    public ResponseEntity<Deduction> updateDeduction(@PathVariable Long id, @RequestBody Deduction deduction) {
        Deduction existing = deductionRepository.findById(id).orElseThrow();
        existing.setDeductionName(deduction.getDeductionName());
        existing.setPercentage(deduction.getPercentage());
        existing.setCode(deduction.getCode());
        return ResponseEntity.ok(deductionRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete deduction", description = "Delete a deduction")
    public ResponseEntity<Void> deleteDeduction(@PathVariable Long id) {
        deductionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 