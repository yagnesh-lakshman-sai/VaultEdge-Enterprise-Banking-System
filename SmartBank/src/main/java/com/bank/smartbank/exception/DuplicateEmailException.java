package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class DuplicateEmailException extends ApplicationException {

	public DuplicateEmailException(String email) {
		super(String.format("Email '%s' is already registered", email));
	}
}
