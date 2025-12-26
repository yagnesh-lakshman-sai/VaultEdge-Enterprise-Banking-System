package com.bank.smartbank.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.smartbank.dto.common.ApiResponse;
import com.bank.smartbank.dto.transaction.TransactionResponse;
import com.bank.smartbank.service.AccountService;
import com.bank.smartbank.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

	private final TransactionService transactionService;
	private final AccountService accountService;
	private final CurrentUser currentUser;

	public TransactionController(TransactionService transactionService, AccountService accountService,
			CurrentUser currentUser) {
		this.transactionService = transactionService;
		this.accountService = accountService;
		this.currentUser = currentUser;
	}

	@GetMapping("/{accountNumber}")
	public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactions(@PathVariable String accountNumber) {

		Long userId = currentUser.getUserId();

		if (!accountService.isAccountOwnedByUser(accountNumber, userId)) {
			return ResponseEntity.status(403).body(ApiResponse.error("You don't have access to this account"));
		}

		List<TransactionResponse> transactions = transactionService.getAccountTransactions(accountNumber);

		return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
	}

	@GetMapping("/{accountNumber}/recent")
	public ResponseEntity<ApiResponse<List<TransactionResponse>>> getRecentTransactions(
			@PathVariable String accountNumber) {

		Long userId = currentUser.getUserId();

		if (!accountService.isAccountOwnedByUser(accountNumber, userId)) {
			return ResponseEntity.status(403).body(ApiResponse.error("Access denied"));
		}

		List<TransactionResponse> transactions = transactionService.getRecentTransactions(accountNumber);

		return ResponseEntity.ok(ApiResponse.success("Recent transactions retrieved", transactions));

	}

	@GetMapping("/{accountId}/range")
	public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByDateRange(
			@PathVariable Long accountId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

		List<TransactionResponse> transactions = transactionService.getTransactionsInDaterange(accountId, startDate,
				endDate);

		return ResponseEntity.ok(ApiResponse.success("Transactions for date range retrieved ", transactions));
	}
}
