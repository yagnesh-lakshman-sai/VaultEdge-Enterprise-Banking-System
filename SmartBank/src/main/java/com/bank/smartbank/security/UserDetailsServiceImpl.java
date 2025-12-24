package com.bank.smartbank.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bank.smartbank.entity.User;
import com.bank.smartbank.exception.UserNotFoundException;
import com.bank.smartbank.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("email", email));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				getAuthorities(user));
	}

	public UserDetails loadUserById(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				getAuthorities(user));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(User user) {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
	}
}
