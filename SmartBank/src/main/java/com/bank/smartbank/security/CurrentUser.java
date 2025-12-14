package com.bank.smartbank.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.bank.smartbank.entity.User;
import com.bank.smartbank.exception.UnauthorizedAccessException;
import com.bank.smartbank.repository.UserRepository;

@Component
public class CurrentUser {

	private final UserRepository userRepository;
	
	public CurrentUser(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public Long getUserId() {
		String email = getEmail();
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UnauthorizedAccessException("User not found"));
		
		return user.getId();
	}
	
	public String getEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthorizedAccessException("No authenticated user found");
		}
		return authentication.getName();
	}
}
