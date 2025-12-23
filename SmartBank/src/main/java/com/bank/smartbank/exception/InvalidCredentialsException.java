package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class InvalidCredentialsException extends ApplicationException {

	public InvalidCredentialsException() {
		super("Invalid email or password");
	}

	public InvalidCredentialsException(String message) {
		super(message);
	}

}
