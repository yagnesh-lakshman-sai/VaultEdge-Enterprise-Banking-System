package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class OtpExpiredException extends ApplicationException {

	public OtpExpiredException() {
		super("OTP has expired. Please request a new one.");
	}

	public OtpExpiredException(String message) {
		super(message);
	}

}
