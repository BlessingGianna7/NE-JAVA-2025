package com.example.erp.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductionRequest {

    @NotBlank(message = "Deduction name is required")
    private String deductionName;

    @NotNull(message = "Percentage is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Percentage must be at least 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Percentage must be at most 100")
    private BigDecimal percentage;
}