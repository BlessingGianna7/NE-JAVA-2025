package com.example.erp.controller;

import com.example.erp.model.Message;
import com.example.erp.model.Employee;
import com.example.erp.repository.MessageRepository;
import com.example.erp.repository.EmployeeRepository;
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
@RequestMapping("/api/messages")
@Tag(name = "Message Management", description = "Message Management API")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Get all messages", description = "Get a list of all messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageRepository.findAll());
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Get messages by employee", description = "Get all messages for an employee")
    public ResponseEntity<List<Message>> getMessagesByEmployee(@PathVariable UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        return ResponseEntity.ok(messageRepository.findByEmployee(employee));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get message by ID", description = "Get a message by ID")
    public ResponseEntity<Message> getMessageById(@PathVariable UUID id) {
        return ResponseEntity.ok(messageRepository.findById(id).orElseThrow());
    }
} 