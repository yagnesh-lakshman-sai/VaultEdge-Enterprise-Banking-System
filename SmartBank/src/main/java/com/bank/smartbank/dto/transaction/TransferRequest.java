package com.bank.smartbank.dto.transaction;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

	@NotBlank(message = "From account is required")
	private String fromAccountNumber;

	@NotBlank(message = "To account is required")
	private String toAccountNumber;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be positive")
	@DecimalMin(value = "1.0", message = "Minimum transfer amount is ₹1")
	@DecimalMax(value = "1000000.0", message = "Maximum transfer amount is ₹10,00,000")
	private BigDecimal amount;

	@Size(max = 500, message = "Description cannot exceed 500 characters")
	private String description;

}
