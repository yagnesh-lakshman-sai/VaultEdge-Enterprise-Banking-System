package com.bank.smartbank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "loans")
public class Loan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "loan_number", unique = true, nullable = false, length = 20)
	private String loanNumber;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
	private BigDecimal interestRate;

	@Column(name = "tenure_months", nullable = false)
	private Integer tenureMonths;

	@Column(name = "emi_amount", precision = 15, scale = 2)
	private BigDecimal emiAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LoanStatus status = LoanStatus.PENDING;

	@Column(length = 1000)
	private String purpose;

	@Column(name = "applied_date", nullable = false, updatable = false)
	private LocalDateTime appliedDate;

	@Column(name = "approved_date")
	private LocalDateTime approvedDate;

	@Column(name = "disbursed_date")
	private LocalDateTime disbursedDate;

	@Column(name = "approved_by")
	private Long approvedBy;

	@Column(name = "rejection_reason", length = 500)
	private String rejectionReason;

	public Loan() {
		this.appliedDate = LocalDateTime.now();
		this.loanNumber = generateLoanNumber();
	}

	private String generateLoanNumber() {
		return "LOAN" + System.currentTimeMillis();
	}
}
