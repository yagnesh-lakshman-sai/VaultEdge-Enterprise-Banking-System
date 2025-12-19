package com.bank.smartbank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.smartbank.dto.auth.LoginRequest;
import com.bank.smartbank.dto.auth.LoginResponse;
import com.bank.smartbank.dto.auth.OtpRequest;
import com.bank.smartbank.dto.auth.RegisterRequest;
import com.bank.smartbank.dto.common.ApiResponse;
import com.bank.smartbank.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest request) {
		authService.register(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse
				.success("Registration successful. Please check your email for OTP to verify your account."));
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<ApiResponse<?>> verifyOtp(@Valid @RequestBody OtpRequest request) {
		authService.verifyOtp(request);

		return ResponseEntity.ok(ApiResponse.success("Email verified successfully. You can now login."));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
		LoginResponse response = authService.login(request);

		return ResponseEntity.ok(ApiResponse.success("Login successful", response));
	}

	@PostMapping("/resend-otp")
	public ResponseEntity<ApiResponse<?>> resendOtp(@RequestParam String email) {
		authService.resendOtp(email);

		return ResponseEntity.ok(ApiResponse.success("OTP has been resent to your email."));
	}

}
