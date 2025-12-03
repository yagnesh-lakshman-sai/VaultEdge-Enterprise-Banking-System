package com.bank.smartbank.exception;

public class AccountInactiveException extends ApplicationException {

	public AccountInactiveException(String accountNumber) {
		super(String.format("Account %s is inactive or frozen", accountNumber));
	}
	
	public AccountInactiveException(String accountNumber, String status) {
        super(String.format("Account %s is %s", accountNumber, status));
    }
}
