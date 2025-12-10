package com.bank.smartbank.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

	private static final SecureRandom secureRandom = new SecureRandom();

	public String generateAccountNumber() {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		int randomDigits = 100 + secureRandom.nextInt(900);

		return Constants.ACCOUNT_NUMBER_PREFIX + timestamp + randomDigits;
	}

	public String generateAccountNumber(String prefix) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		int randomDigits = 100 + secureRandom.nextInt(900);

		return prefix + timestamp + randomDigits;
	}

	public String generateSimpleAccountNumber() {
		StringBuilder accountNumber = new StringBuilder();

		for (int i = 0; i < 16; i++) {
			accountNumber.append(secureRandom.nextInt(10));
		}
		return accountNumber.toString();
	}
}
