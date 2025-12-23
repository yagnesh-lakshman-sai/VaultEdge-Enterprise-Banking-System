package com.bank.smartbank.dto.loan;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

	@NotNull(message = "Loan amount is required")
	@Positive(message = "Amount must be positive")
	@DecimalMin(value = "10000.0", message = "Minimum loan amount is ₹10,000")
	@DecimalMax(value = "10000000.0", message = "Maximum loan amount is ₹1,00,00,000")
	private BigDecimal amount;

	@NotNull(message = "Tenure is required")
	@Min(value = 6, message = "Minimum tenure is 6 months")
	@Max(value = 360, message = "Maximum tenure is 360 months (30 years)")
	private Integer tenureMonths;

	@NotBlank(message = "Purpose is required")
	@Size(min = 10, max = 1000, message = "Purpose must be 10-1000 characters")
	private String purpose;

}
