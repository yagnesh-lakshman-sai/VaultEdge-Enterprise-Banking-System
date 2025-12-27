package com.bank.smartbank.controller;

import com.bank.smartbank.entity.User;
import com.bank.smartbank.exception.UnauthorizedAccessException;
import com.bank.smartbank.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

	private final UserRepository userRepository;

	public CurrentUser(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	private Authentication getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			throw new UnauthorizedAccessException("No authenticated user found");
		}
		return auth;
	}

	public Long getUserId() {
		String email = getEmail();
		return userRepository.findByEmail(email).map(User::getId)
				.orElseThrow(() -> new UnauthorizedAccessException("User not found for email: " + email));
	}

	public String getEmail() {
		return getAuthentication().getName();
	}

	public String getRole() {
		return getAuthentication().getAuthorities().iterator().next().getAuthority();
	}

	public String getRoleWithoutPrefix() {
		return getRole().replace("ROLE_", "");
	}

	public User getCurrentUser() {
		String email = getEmail();
		return userRepository.findByEmail(email).orElseThrow(() -> new UnauthorizedAccessException("User not found"));
	}

	public boolean hasRole(String role) {
		return getRole().equals("ROLE_" + role);
	}

	public boolean isAdmin() {
		return hasRole("ADMIN");
	}

	public boolean isCustomer() {
		return hasRole("CUSTOMER");
	}
}