package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class InvalidOtpException extends ApplicationException {

	public InvalidOtpException() {
		super("Invalid OTP. Please check and try again.");
	}

	public InvalidOtpException(String message) {
		super(message);
	}
}
