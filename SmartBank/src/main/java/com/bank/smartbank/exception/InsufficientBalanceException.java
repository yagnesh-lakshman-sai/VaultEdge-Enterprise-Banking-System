package com.bank.smartbank.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends ApplicationException {

	public InsufficientBalanceException(String message) {
		super(message);
	}
	
	public InsufficientBalanceException(BigDecimal available, BigDecimal required) {
		super(String.format("Insufficient balance. Available: ₹%s, Required: ₹%s",available, required));
	}
	
}
