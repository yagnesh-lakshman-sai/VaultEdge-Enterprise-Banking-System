package com.bank.smartbank.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

	private String token;
	private String tokenType= "Bearer";
	private Long userId;
	private String email;
	private String fullName;
	private  String role;
	
	public LoginResponse() {
    }
    
    public LoginResponse(String token, Long userId, String email, String fullName, String role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }
}
