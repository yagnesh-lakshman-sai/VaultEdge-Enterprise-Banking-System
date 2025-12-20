package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
