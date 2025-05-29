package com.example.erp.controller;

import com.example.erp.model.Employment;
import com.example.erp.dto.EmploymentRequest;
import com.example.erp.service.EmploymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/employments")
@Tag(name = "Employment Management", description = "Employment Management API")
@SecurityRequirement(name = "bearerAuth")
public class EmploymentController {
    @Autowired
    private EmploymentService employmentService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Get all employments", description = "Get a list of all employments")
    public ResponseEntity<List<Employment>> getAllEmployments() {
        return ResponseEntity.ok(employmentService.getAllEmployments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Get employment by ID", description = "Get an employment by ID")
    public ResponseEntity<Employment> getEmploymentById(@PathVariable UUID id) {
        return ResponseEntity.ok(employmentService.getEmploymentById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create employment", description = "Create a new employment record")
    public ResponseEntity<Employment> createEmployment(@RequestBody EmploymentRequest request) {
        return ResponseEntity.ok(employmentService.createEmployment(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Update employment", description = "Update an employment record")
    public ResponseEntity<Employment> updateEmployment(@PathVariable UUID id, @RequestBody EmploymentRequest request) {
        return ResponseEntity.ok(employmentService.updateEmployment(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Delete employment", description = "Delete an employment record")
    public ResponseEntity<Void> deleteEmployment(@PathVariable UUID id) {
        employmentService.deactivateEmployment(id);
        return ResponseEntity.noContent().build();
    }
} 