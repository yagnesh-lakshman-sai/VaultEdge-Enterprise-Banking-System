package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class LoanNotFoundException extends ResourceNotFoundException {

	public LoanNotFoundException(String message) {
		super(message);
	}

	public LoanNotFoundException(Long loanId) {
		super("Loan", "id", loanId);
	}

	public LoanNotFoundException(String fieldName, Object fieldValue) {
		super("Loan", fieldName, fieldValue);
	}
}
