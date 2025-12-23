package com.bank.smartbank.dto.loan;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApprovalRequest {

	@NotNull(message = "Approval decision is required")
	private Boolean approved;

	@Size(max = 500, message = "Remarks cannot exceed 500 characters")
	private String remarks;

}
