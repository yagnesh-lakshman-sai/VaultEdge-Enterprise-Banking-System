package com.bank.smartbank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType type;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(name = "balance_after", nullable = false, precision = 15, scale = 2)
	private BigDecimal balanceAfter;

	@Column(length = 500)
	private String description;

	@Column(name = "reference_account", length = 20)
	private String referenceAccount;

	@Column(name = "transaction_ref", unique = true, nullable = false, length = 30)
	private String transactionRef;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public Transaction() {
		this.createdAt = LocalDateTime.now();
		this.transactionRef = generateTransactionRef();
	}

	private String generateTransactionRef() {
		return "TXN" + System.currentTimeMillis();
	}
}
