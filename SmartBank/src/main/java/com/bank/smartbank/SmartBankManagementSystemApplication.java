package com.bank.smartbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartBankManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartBankManagementSystemApplication.class, args);
	}

}
