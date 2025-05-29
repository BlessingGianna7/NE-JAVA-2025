package com.example.erp.util;

import com.example.erp.model.Payslip;
import com.example.erp.model.Employee;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;

public class PayslipPdfUtil {
    public static byte[] generatePayslipPdf(Payslip payslip) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        Employee employee = payslip.getEmployee();
        document.add(new Paragraph("Payslip"));
        document.add(new Paragraph("------------------------------"));
        document.add(new Paragraph("Employee: " + employee.getFirstName() + " " + employee.getLastName()));
        document.add(new Paragraph("Employee Code: " + employee.getCode()));
        document.add(new Paragraph("Month/Year: " + payslip.getMonth() + "/" + payslip.getYear()));
        document.add(new Paragraph("Status: " + payslip.getStatus()));
        document.add(new Paragraph(""));
        document.add(new Paragraph("Base Salary: " + payslip.getGrossSalary().subtract(payslip.getHouseAmount()).subtract(payslip.getTransportAmount())));
        document.add(new Paragraph("Housing: " + payslip.getHouseAmount()));
        document.add(new Paragraph("Transport: " + payslip.getTransportAmount()));
        document.add(new Paragraph("Gross Salary: " + payslip.getGrossSalary()));
        document.add(new Paragraph("Employee Tax: " + payslip.getEmployeeTaxedAmount()));
        document.add(new Paragraph("Pension: " + payslip.getPensionAmount()));
        document.add(new Paragraph("Medical Insurance: " + payslip.getMedicalInsuranceAmount()));
        document.add(new Paragraph("Others: " + payslip.getOtherTaxedAmount()));
        document.add(new Paragraph("Net Salary: " + payslip.getNetSalary()));
        document.close();
        return baos.toByteArray();
    }
} 