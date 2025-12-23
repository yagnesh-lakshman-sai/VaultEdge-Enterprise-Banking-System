package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class TransactionNotFoundException extends ResourceNotFoundException {

	public TransactionNotFoundException(String message) {
		super(message);
	}

}
