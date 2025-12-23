package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class AccountNotFoundException extends ResourceNotFoundException {

	public AccountNotFoundException(String message) {
		super(message);
	}

	public AccountNotFoundException(Long accountId) {
		super("Account", "id", accountId);
	}

	public AccountNotFoundException(String fieldName, Object fieldValue) {
		super("Account", fieldName, fieldValue);
	}
}
