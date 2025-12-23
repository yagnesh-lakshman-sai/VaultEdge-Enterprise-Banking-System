package com.bank.smartbank.dto.account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bank.smartbank.entity.Account;

import lombok.*;

@Data
@NoArgsConstructor
public class AccountResponse {

	private Long id;
	private String accountNumber;
	private BigDecimal balance;
	private String type;
	private String status;
	private LocalDateTime createdAt;

	public AccountResponse(Account account) {
		this.id = account.getId();
		this.accountNumber = account.getAccountNumber();
		this.balance = account.getBalance();
		this.type = account.getType().name();
		this.status = account.getStatus().name();
		this.createdAt = account.getCreatedAt();
	}
}
