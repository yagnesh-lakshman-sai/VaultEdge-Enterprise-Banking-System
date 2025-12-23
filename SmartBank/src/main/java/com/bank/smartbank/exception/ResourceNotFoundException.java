package com.bank.smartbank.exception;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends ApplicationException {

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
	}
}
