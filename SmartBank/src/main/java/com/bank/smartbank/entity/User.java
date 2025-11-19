package com.bank.smartbank.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Role;

public class User {

	private long id;
	
	private String email;
	
	private String password;
	
	private String fullName;
	
	private String  phine;
	
	private Role role = Role.CUSTOMER;
	
	private LocalDateTime createAt;
	
	private Boolean isVerified;
	
	private List<Account> accounts;
	
	public User() {
	
		this createdAt = localDateTime.now();
	}
	
	
