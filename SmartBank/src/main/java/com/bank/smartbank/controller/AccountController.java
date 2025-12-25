package com.bank.smartbank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.smartbank.dto.account.AccountResponse;
import com.bank.smartbank.dto.account.CreateAccountRequest;
import com.bank.smartbank.dto.common.ApiResponse;
import com.bank.smartbank.service.AccountService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

	private final AccountService accountService;
	private final CurrentUser currentUser;

	public AccountController(AccountService accountService, CurrentUser currentUser) {
		this.accountService = accountService;
		this.currentUser = currentUser;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
			@Valid @RequestBody CreateAccountRequest request) {

		Long userId = currentUser.getUserId();

		AccountResponse account = accountService.createAccount(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Account created successfully", account));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<AccountResponse>>> getUserAccounts() {
		Long userId = currentUser.getUserId();

		List<AccountResponse> accounts = accountService.getUserAccounts(userId);
		return ResponseEntity.ok(ApiResponse.success("Accounts retrieved successfully", accounts));
	}

	@GetMapping("/{accountNumber}")
	public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable String accountNumber) {

		Long userId = currentUser.getUserId();

		if (!accountService.isAccountOwnedByUser(accountNumber, userId)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(ApiResponse.error("You don't have access to this account"));
		}

		AccountResponse account = accountService.getAccountNumber(accountNumber);

		return ResponseEntity.ok(ApiResponse.success("Account details retrieved", account));
	}

}