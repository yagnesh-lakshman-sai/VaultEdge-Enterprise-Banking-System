package com.bank.smartbank.dto.loan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bank.smartbank.entity.Loan;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponse {

	private Long id;
	private String loanNumber;
	private String customerName;
	private BigDecimal interestRate;
	private BigDecimal amount;
	private Integer tenureMonths;
	private BigDecimal emiAmount;
	private String status;
	private String purpose;
	private LocalDateTime appliedDate;
	private LocalDateTime approvedDate;
	private LocalDateTime disbursedDate;
	private String rejectionReason;

	public LoanResponse(Loan loan) {
		this.id = loan.getId();
		this.loanNumber = loan.getLoanNumber();
		this.customerName = loan.getUser() != null ? loan.getUser().getFullName() : "Unknown";
		this.amount = loan.getAmount();
		this.interestRate = loan.getInterestRate();
		this.tenureMonths = loan.getTenureMonths();
		this.emiAmount = loan.getEmiAmount();
		this.status = loan.getStatus().name();
		this.purpose = loan.getPurpose();
		this.appliedDate = loan.getAppliedDate();
		this.approvedDate = loan.getApprovedDate();
		this.disbursedDate = loan.getDisbursedDate();
		this.rejectionReason = loan.getRejectionReason();
	}
}
