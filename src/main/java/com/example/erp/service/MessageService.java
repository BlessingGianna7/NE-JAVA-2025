package com.example.erp.service;

import com.example.erp.model.Employee;
import com.example.erp.model.Message;
import com.example.erp.model.Payslip;
import com.example.erp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void createPayrollApprovalMessage(Payslip payslip) {
        Employee employee = payslip.getEmployee();
        String msg = String.format(
            "Dear %s, your salary for %02d/%d from INSTITUTION amounting to %.0f RWF has been credited to your account %s successfully.",
            employee.getFirstName(), payslip.getMonth(), payslip.getYear(), payslip.getNetSalary(), employee.getCode()
        );
        Message message = new Message();
        message.setEmployee(employee);
        message.setMessage(msg);
        message.setMonth(payslip.getMonth());
        message.setYear(payslip.getYear());
        message.setSent(false);
        message.setCreatedAt(new java.util.Date());
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(employee.getEmail());
            mailMessage.setSubject("Salary Credited - Payslip for " + payslip.getMonth() + "/" + payslip.getYear());
            mailMessage.setText(msg);
            mailSender.send(mailMessage);
            message.setSent(true);
        } catch (Exception e) {
            // Log error if needed
            message.setSent(false);
        }
        messageRepository.save(message);
    }
} 