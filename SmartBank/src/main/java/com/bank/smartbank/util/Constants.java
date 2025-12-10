package com.bank.smartbank.util;

import java.math.BigDecimal;

public final class Constants {

	private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
	
	public static final BigDecimal MINIMUM_BALANCE = new BigDecimal("1000.00");
    public static final BigDecimal MINIMUM_TRANSFER_AMOUNT = new BigDecimal("1.00");
    public static final BigDecimal MAXIMUM_TRANSFER_AMOUNT = new BigDecimal("1000000.00");
    public static final BigDecimal DAILY_TRANSFER_LIMIT = new BigDecimal("500000.00");
    
    public static final String ACCOUNT_NUMBER_PREFIX = "ACC";
    public static final int ACCOUNT_NUMBER_LENGTH = 16;  // ACC + 13 digits
    
    //  LOAN CONSTANTS 
    
    public static final BigDecimal MINIMUM_LOAN_AMOUNT = new BigDecimal("10000.00");
    public static final BigDecimal MAXIMUM_LOAN_AMOUNT = new BigDecimal("10000000.00");
    public static final int MINIMUM_LOAN_TENURE_MONTHS = 6;
    public static final int MAXIMUM_LOAN_TENURE_MONTHS = 360;  // 30 years
    
    public static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("12.50");  // 12.5%
    public static final BigDecimal PREMIUM_INTEREST_RATE = new BigDecimal("10.50");  // 10.5%
    
    //  OTP CONSTANTS 
    
    public static final int OTP_LENGTH = 6;
    public static final long OTP_VALIDITY_MINUTES = 5;
    public static final int MAX_OTP_ATTEMPTS = 3;
    
    //  JWT CONSTANTS 
    
    public static final long JWT_EXPIRATION_MS = 86400000;  // 24 hours
    public static final String JWT_SECRET_KEY = "SmartBankSecretKey2024ForJWTTokenGeneration";  // Should be in env variables in production
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER = "Authorization";
    
    //  VALIDATION CONSTANTS 
    
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String PHONE_REGEX = "^[0-9]{10}$";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
    
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 50;
    
    //  DATE/TIME CONSTANTS 
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    //  EMAIL CONSTANTS 
    
    public static final String EMAIL_FROM = "noreply@smartbank.com";
    public static final String EMAIL_SUBJECT_OTP = "Your Smart Bank OTP Code";
    public static final String EMAIL_SUBJECT_WELCOME = "Welcome to Smart Bank!";
    public static final String EMAIL_SUBJECT_TRANSFER = "Transfer Confirmation";
    public static final String EMAIL_SUBJECT_LOAN_APPROVED = "Loan Application Approved";
    
    //  BUSINESS RULES
    
    public static final int MINIMUM_ACCOUNT_AGE_FOR_LOAN_DAYS = 180;  // 6 months
    public static final BigDecimal MINIMUM_AVERAGE_BALANCE_FOR_LOAN = new BigDecimal("10000.00");
}
