package com.bank.smartbank.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
public class TransferResponse {

	private String transactionRef;
	private String fromAccountNumber;
	private String toAccountNumber;
	private BigDecimal amount;
	private BigDecimal newBalance;
	private LocalDateTime timestamp;
	private String message;

	public TransferResponse(String transactionRef, String fromAccountNumber, String toAccountNumber, BigDecimal amount,
			BigDecimal newBalance, String message) {
		this.timestamp = LocalDateTime.now();
		this.transactionRef = transactionRef;
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.amount = amount;
		this.newBalance = newBalance;
		this.message = message;
	}
}
