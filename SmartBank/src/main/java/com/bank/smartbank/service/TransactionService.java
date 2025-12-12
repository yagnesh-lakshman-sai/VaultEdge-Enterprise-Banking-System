package com.bank.smartbank.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bank.smartbank.exception.AccountNotFoundException;
import com.bank.smartbank.dto.transaction.TransactionResponse;
import com.bank.smartbank.entity.Account;
import com.bank.smartbank.entity.Transaction;
import com.bank.smartbank.entity.TransactionType;
import com.bank.smartbank.repository.AccountRepository;
import com.bank.smartbank.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;

	public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
	}

	public Transaction recordTransaction(Account account, TransactionType type, BigDecimal amount,
			BigDecimal balanceAfter, String description, String referenceAmount) {
		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setType(type);
		transaction.setAmount(amount);
		transaction.setBalanceAfter(balanceAfter);
		transaction.setDescription(description);
		transaction.setReferenceAccount(referenceAmount);

		return transactionRepository.save(transaction);
	}

	public List<TransactionResponse> getAccountTransactions(String accountNumber) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountNotFoundException("accountNumber", accountNumber));

		List<Transaction> transactions = transactionRepository.findByAccountOrderByCreatedAtDesc(account);

		return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
	}

	public List<TransactionResponse> getTransactionsInDaterange(Long accountId, LocalDateTime startDate,
			LocalDateTime endDate) {

		List<Transaction> transactions = transactionRepository.findByAccountAndDateRange(accountId, startDate, endDate);

		return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
	}

	public List<TransactionResponse> getRecentTransactions(String accountNumber) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountNotFoundException("accountNumber", accountNumber));

		List<Transaction> transactions = transactionRepository.findByAccountOrderByCreatedAtDesc(account).stream()
				.limit(10).collect(Collectors.toList());
		return transactions.stream().map(TransactionResponse::new).collect(Collectors.toList());
	}
}
