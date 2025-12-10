package com.bank.smartbank.util;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class OtpGenerator {

	private static final SecureRandom secureRandom = new SecureRandom();

	public String generateOtp() {
		return generateOtp(Constants.OTP_LENGTH);
	}

	public String generateOtp(int length) {
		StringBuilder otp = new StringBuilder();
		for (int i = 0; i < length; i++) {
			otp.append(secureRandom.nextInt(10));
		}
		return otp.toString();
	}

	public String generateNumericOtp() {
		int otp = 100000 + secureRandom.nextInt(900000);
		return String.valueOf(otp);
	}
}
