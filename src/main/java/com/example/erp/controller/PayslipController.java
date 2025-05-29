package com.example.erp.controller;

import com.example.erp.model.Payslip;
import com.example.erp.model.Employee;
import com.example.erp.repository.PayslipRepository;
import com.example.erp.repository.EmployeeRepository;
import com.example.erp.service.PayslipService;
import com.example.erp.service.MessageService;
import com.example.erp.util.PayslipPdfUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payslips")
@Tag(name = "Payslip Management", description = "Payslip Management API")
@SecurityRequirement(name = "bearerAuth")
public class PayslipController {
    @Autowired
    private PayslipRepository payslipRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PayslipService payslipService;
    @Autowired
    private MessageService messageService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Get all payslips", description = "Get a list of all payslips")
    public ResponseEntity<List<Payslip>> getAllPayslips() {
        return ResponseEntity.ok(payslipRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get payslip by ID", description = "Get a payslip by ID")
    public ResponseEntity<Payslip> getPayslipById(@PathVariable UUID id) {
        return ResponseEntity.ok(payslipRepository.findById(id).orElseThrow());
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Get payslips by employee", description = "Get all payslips for an employee")
    public ResponseEntity<List<Payslip>> getPayslipsByEmployee(@PathVariable UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        return ResponseEntity.ok(payslipRepository.findByEmployee(employee));
    }

    @GetMapping("/month-year")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Get payslips by month and year", description = "Get all payslips for a given month and year")
    public ResponseEntity<List<Payslip>> getPayslipsByMonthYear(@RequestParam Integer month, @RequestParam Integer year) {
        return ResponseEntity.ok(payslipRepository.findByMonthAndYear(month, year));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve payslip", description = "Approve a payslip (set status to PAID)")
    public ResponseEntity<Payslip> approvePayslip(@PathVariable UUID id) {
        Payslip payslip = payslipRepository.findById(id).orElseThrow();
        payslip.setStatus(Payslip.PayslipStatus.PAID);
        Payslip saved = payslipRepository.save(payslip);
        messageService.createPayrollApprovalMessage(saved);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Generate payroll for a month/year", description = "Manager triggers payroll generation for all active employees for a given month and year.")
    public ResponseEntity<Void> generatePayroll(@RequestParam Integer month, @RequestParam Integer year) {
        payslipService.generatePayroll(month, year);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Download payslip as PDF", description = "Download a payslip as a PDF file")
    public ResponseEntity<byte[]> downloadPayslipPdf(@PathVariable UUID id) {
        Payslip payslip = payslipRepository.findById(id).orElseThrow();
        try {
            byte[] pdfBytes = PayslipPdfUtil.generatePayslipPdf(payslip);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "payslip-" + id + ".pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 