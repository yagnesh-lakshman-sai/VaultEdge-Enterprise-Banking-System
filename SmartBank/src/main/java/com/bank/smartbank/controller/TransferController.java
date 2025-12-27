package com.bank.smartbank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.smartbank.dto.common.ApiResponse;
import com.bank.smartbank.dto.transaction.TransferRequest;
import com.bank.smartbank.dto.transaction.TransferResponse;
import com.bank.smartbank.service.AccountService;
import com.bank.smartbank.service.TransferService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transfer")
@CrossOrigin(origins = "*")
public class TransferController {

	private final TransferService transferService;
	private final AccountService accountService;
	private final CurrentUser currentUser;

	public TransferController(TransferService transferService, AccountService accountService, CurrentUser currentUser) {
		this.transferService = transferService;
		this.accountService = accountService;
		this.currentUser = currentUser;
	}

	@PostMapping
	public ResponseEntity<ApiResponse<TransferResponse>> transfer(@Valid @RequestBody TransferRequest request) {

		Long userId = currentUser.getUserId();

		if (!accountService.isAccountOwnedByUser(request.getFromAccountNumber(), userId)) {
			return ResponseEntity.status(403).body(ApiResponse.error("You don't have access to the sender account"));
		}

		TransferResponse response = transferService.transfer(request);

		return ResponseEntity.ok(ApiResponse.success("Transfer completed successfully", response));
	}
}
