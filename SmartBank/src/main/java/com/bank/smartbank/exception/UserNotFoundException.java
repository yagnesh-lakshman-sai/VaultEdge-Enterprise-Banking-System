package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends ResourceNotFoundException {

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Long userId) {
		super("User", "id", userId);
	}

	public UserNotFoundException(String fieldName, Object fieldValue) {
		super("User", fieldName, fieldValue);
	}
}
