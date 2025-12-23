package com.bank.smartbank.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

	@NotBlank(message = "Account type is required")
	@Pattern(regexp = "SAVINGS|CURRENT|FIXED_DEPOSIT", message = "Invalid account type")
	private String type;

}
