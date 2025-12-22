package com.bank.smartbank.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

	private String token;
	private String tokenType = "Bearer";
	private Long userId;
	private String email;
	private String fullName;
	private String role;

}
