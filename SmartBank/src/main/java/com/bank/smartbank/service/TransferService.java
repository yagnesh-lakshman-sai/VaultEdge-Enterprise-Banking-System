package com.bank.smartbank.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.bank.smartbank.dto.transaction.TransferRequest;
import com.bank.smartbank.dto.transaction.TransferResponse;
import com.bank.smartbank.entity.Account;
import com.bank.smartbank.entity.AccountStatus;
import com.bank.smartbank.entity.Transaction;
import com.bank.smartbank.entity.TransactionType;
import com.bank.smartbank.exception.AccountInactiveException;
import com.bank.smartbank.exception.AccountNotFoundException;
import com.bank.smartbank.exception.InsufficientBalanceException;
import com.bank.smartbank.repository.AccountRepository;
import com.bank.smartbank.util.EmailService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransferService {

	private final AccountRepository accountRepository;
	private final TransactionService transactionService;
	private final EmailService emailService;

	public TransferService(AccountRepository accountRepository, TransactionService transactionService,
			EmailService emailService) {
		this.accountRepository = accountRepository;
		this.transactionService = transactionService;
		this.emailService = emailService;
	}

	public TransferResponse transfer(TransferRequest request) {

		Account senderAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
				.orElseThrow(() -> new AccountNotFoundException("accountNumber", request.getFromAccountNumber()));

		Account receiverAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
				.orElseThrow(() -> new AccountNotFoundException("accountNumber", request.getToAccountNumber()));

		if (senderAccount.getStatus() != AccountStatus.ACTIVE) {
			throw new AccountInactiveException(senderAccount.getAccountNumber(), senderAccount.getStatus().name());
		}

		if (receiverAccount.getStatus() != AccountStatus.ACTIVE) {
			throw new AccountInactiveException(receiverAccount.getAccountNumber(), receiverAccount.getStatus().name());
		}

		if (senderAccount.getBalance().compareTo(request.getAmount()) < 0) {
			throw new InsufficientBalanceException(senderAccount.getBalance(), request.getAmount());
		}

		BigDecimal senderNewBalance = senderAccount.getBalance().subtract(request.getAmount());
		BigDecimal receiverNewBalance = receiverAccount.getBalance().add(request.getAmount());

		senderAccount.setBalance(senderNewBalance);
		receiverAccount.setBalance(receiverNewBalance);

		accountRepository.save(senderAccount);
		accountRepository.save(receiverAccount);

		Transaction senderTransaction = transactionService
				.recordTransaction(senderAccount, TransactionType.TRANSFER_DEBIT, request.getAmount().negate(),
						senderNewBalance,
						request.getDescription() != null ? request.getDescription()
								: "Transfer to " + receiverAccount.getAccountNumber(),
						receiverAccount.getAccountNumber());

		transactionService
				.recordTransaction(receiverAccount, TransactionType.TRANSFER_CREDIT, request.getAmount().negate(),
						senderNewBalance,
						request.getDescription() != null ? request.getDescription()
								: "Transfer to " + receiverAccount.getAccountNumber(),
						senderAccount.getAccountNumber());

		emailService.sendTransferConfirmationEmail(senderAccount.getUser().getEmail(),
				senderTransaction.getTransactionRef(), request.getAmount().toString());

		TransferResponse response = new TransferResponse();
		response.setTransactionRef(senderTransaction.getTransactionRef());
		response.setFromAccountNumber(request.getFromAccountNumber());
		response.setToAccountNumber(request.getToAccountNumber());
		response.setAmount(request.getAmount());
		response.setNewBalance(senderNewBalance);
		response.setMessage("Transfer completed successfully");

		System.out.println(" Transfer completed : " + senderTransaction.getTransactionRef());

		return response;
	}
}
