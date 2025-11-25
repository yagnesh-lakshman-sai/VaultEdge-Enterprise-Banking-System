package com.bank.smartbank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "account_number", unique= true, nullable= false, length= 20)
	private String accountNumber;
	
	@Column(nullable = false , precision = 15 , scale = 2)
	private BigDecimal balance = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountType type;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountStatus status = AccountStatus.ACTIVE;
	
	@Column(name = "created_at" ,nullable = false ,updatable = false)
	private LocalDateTime createdAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Transaction> transactions = new ArrayList<>();
	
	public Account() {
		this.createdAt = LocalDateTime.now();
	}
}
