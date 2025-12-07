package com.bank.smartbank.exception;

public class TransactionNotFoundException extends ResourceNotFoundException {
    
    public TransactionNotFoundException(String message) {
        super(message);
    }
    
//    public TransactionNotFoundException(String transactionRef) {
//        super("Transaction", "transactionRef", transactionRef);
//    }
}
