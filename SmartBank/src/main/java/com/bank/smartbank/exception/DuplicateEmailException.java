package com.bank.smartbank.exception;

public class DuplicateEmailException extends ApplicationException {

	public DuplicateEmailException(String email) {
		super(String.format("Email '%s' is already registered", email));
	}
}
