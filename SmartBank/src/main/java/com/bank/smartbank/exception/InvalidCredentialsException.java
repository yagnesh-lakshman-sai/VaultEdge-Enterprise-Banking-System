package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class InvalidCredentialsException extends ApplicationException {

	public InvalidCredentialsException() {
		super("Invsclid email or password");
	}
	
	public InvalidCredentialsException(String message) {
		super(message);
	}
	
}
