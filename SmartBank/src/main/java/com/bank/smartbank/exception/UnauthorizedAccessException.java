package com.bank.smartbank.exception;

public class UnauthorizedAccessException extends ApplicationException  {

	public UnauthorizedAccessException() {
        super("You are not authorized to access this resource");
    }
    
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
