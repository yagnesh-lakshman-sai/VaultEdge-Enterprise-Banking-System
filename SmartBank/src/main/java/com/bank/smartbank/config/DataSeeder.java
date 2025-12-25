package com.bank.smartbank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bank.smartbank.entity.Role;
import com.bank.smartbank.entity.User;
import com.bank.smartbank.repository.UserRepository;

@Configuration
public class DataSeeder {

	private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {

			if (!userRepository.existsByEmail("admin@bank.com")) {
				User admin = new User();
				admin.setEmail("admin@bank.com");
				admin.setPassword(passwordEncoder.encode("Admin@123"));
				admin.setFullName("Admin User");
				admin.setPhone("9999955555");
				admin.setRole(Role.ADMIN);
				admin.setIsVerified(true);

				userRepository.save(admin);

				logger.info("  Admin user created successfully");
				logger.info("  Email: admin@bank.com");
				logger.info("  Password: Admin@123");
			} else {
				logger.info("Admin user already exists");
			}
		};
	}
}
