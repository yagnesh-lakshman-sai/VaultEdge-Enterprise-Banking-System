package com.bank.smartbank.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.smartbank.dto.auth.LoginRequest;
import com.bank.smartbank.dto.auth.LoginResponse;
import com.bank.smartbank.dto.auth.OtpRequest;
import com.bank.smartbank.dto.auth.RegisterRequest;
import com.bank.smartbank.entity.Role;
import com.bank.smartbank.entity.User;
import com.bank.smartbank.exception.DuplicateEmailException;
import com.bank.smartbank.exception.InvalidCredentialsException;
import com.bank.smartbank.exception.InvalidOtpException;
import com.bank.smartbank.exception.UserNotFoundException;
import com.bank.smartbank.repository.UserRepository;
import com.bank.smartbank.security.JwtTokenProvider;
import com.bank.smartbank.util.EmailService;
import com.bank.smartbank.util.OtpGenerator;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final OtpGenerator otpGenerator;
	private final EmailService emailService;

	private final Map<String, OtpData> otpStore = new HashMap<>();

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider, OtpGenerator otpGenerator, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.otpGenerator = otpGenerator;
		this.emailService = emailService;
	}

//	Register new user logic

	public void register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new DuplicateEmailException(request.getEmail());
		}

		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFullName(request.getFullName());
		user.setPhone(request.getPhone());
		user.setRole(Role.CUSTOMER);
		user.setIsVerified(false);

		userRepository.save(user);

		String otp = otpGenerator.generateOtp();
		storeOtp(request.getEmail(), otp);
		emailService.sendOtpEmail(request.getEmail(), otp);

		System.out.println("User registered: " + request.getEmail());
		System.out.println("OTP sent: " + otp);
	}

//	Verify OTP logic

	public void verifyOtp(OtpRequest request) {
		OtpData otpData = otpStore.get(request.getEmail());

		if (otpData == null) {
			throw new InvalidOtpException("No OTP found for this email");
		}

		if (otpData.isExpired()) {
			otpStore.remove(request.getEmail());
			throw new InvalidOtpException("OTP has expired");
		}

		if (!otpData.getOtp().equals(request.getOtp())) {
			throw new InvalidOtpException("Invalid OTP");
		}

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UserNotFoundException("email", request.getEmail()));

		user.setIsVerified(true);
		userRepository.save(user);

		otpStore.remove(request.getEmail());

		emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

		System.out.println("User verified: " + request.getEmail());
	}

//	Login user

	public LoginResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new InvalidCredentialsException());

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException();
		}

		String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole().name());

		LoginResponse response = new LoginResponse();
		response.setToken(token);
		response.setUserId(user.getId());
		response.setEmail(user.getEmail());
		response.setFullName(user.getFullName());
		response.setRole(user.getRole().name());

		System.out.println("User logged in: " + user.getEmail());

		return response;
	}

//	Resend OTP logic 

	public void resendOtp(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("email", email));

		if (user.getIsVerified()) {
			throw new InvalidOtpException("User is already verified");
		}

		String otp = otpGenerator.generateOtp();
		storeOtp(email, otp);
		emailService.sendOtpEmail(email, otp);
		System.out.println("OTP resent to: " + email + " - " + otp);
	}

//------	HELPER METHODS ------- 

//	Store OTP with expiration time

	private void storeOtp(String email, String otp) {
		LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
		otpStore.put(email, new OtpData(otp, expiryTime));
	}

//	 Inner class to store OTP data

	private static class OtpData {
		private final String otp;
		private final LocalDateTime expiryTime;

		public OtpData(String otp, LocalDateTime expiryTime) {
			this.otp = otp;
			this.expiryTime = expiryTime;
		}

		public String getOtp() {
			return otp;
		}

		public boolean isExpired() {
			return LocalDateTime.now().isAfter(expiryTime);
		}
	}
}
