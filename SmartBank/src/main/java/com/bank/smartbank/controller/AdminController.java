package com.bank.smartbank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.bank.smartbank.dto.common.ApiResponse;
import com.bank.smartbank.dto.loan.LoanApprovalRequest;
import com.bank.smartbank.dto.loan.LoanResponse;
import com.bank.smartbank.entity.LoanStatus;
import com.bank.smartbank.service.LoanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final LoanService loanService;
	private final CurrentUser currentUser;

	public AdminController(LoanService loanService, CurrentUser currentUser) {
		this.loanService = loanService;
		this.currentUser = currentUser;
	}

	@GetMapping("/loans/pending")
	public ResponseEntity<ApiResponse<List<LoanResponse>>> getPendingLoans() {

		List<LoanResponse> loans = loanService.getPendingLoans();

		return ResponseEntity.ok(ApiResponse.success("Pending loans retrieved succesfully", loans));
	}

	@GetMapping("/loans")
	public ResponseEntity<ApiResponse<List<LoanResponse>>> getLoansByStatus(@RequestParam LoanStatus status) {

		List<LoanResponse> loans = loanService.getLoansByStatus(status);

		return ResponseEntity.ok(ApiResponse.success("Loans with status " + status + " retrieved", loans));
	}

	@GetMapping("/loans/all")
	public ResponseEntity<ApiResponse<List<LoanResponse>>> getAllLoans() {

		List<LoanResponse> loans = loanService.getPendingLoans();

		return ResponseEntity.ok(ApiResponse.success("All Loans retrieved", loans));
	}

	@PutMapping("/loans/{loanId}/approve")
	public ResponseEntity<ApiResponse<LoanResponse>> approveLoan(@PathVariable Long loanId,
			@Valid @RequestBody LoanApprovalRequest request) {

		Long adminId = currentUser.getUserId();

		LoanResponse loan = loanService.approveLoan(loanId, adminId, request);

		String message = request.getApproved() ? "Loan approved successfully" : "Loan rejected successfully";
		return ResponseEntity.ok(ApiResponse.success(message, loan));
	}

	@PutMapping("/loans/{loanId}/review")
	public ResponseEntity<ApiResponse<LoanResponse>> markedUnderReview(@PathVariable Long loanId) {

		LoanResponse loan = loanService.markUnderReview(loanId);

		return ResponseEntity.ok(ApiResponse.success("Loan marked as under review ", loan));
	}

	@GetMapping("/loans/{loanId}")
	public ResponseEntity<ApiResponse<LoanResponse>> getLoanDetails(@PathVariable Long loanId) {

		LoanResponse loan = loanService.getLoanById(loanId);

		return ResponseEntity.ok(ApiResponse.success("Loan details retrieved", loan));
	}

}
