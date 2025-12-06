package com.bank.smartbank.exception;

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
