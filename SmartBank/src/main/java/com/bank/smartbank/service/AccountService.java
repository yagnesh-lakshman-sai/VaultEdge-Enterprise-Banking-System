package com.bank.smartbank.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bank.smartbank.dto.account.AccountResponse;
import com.bank.smartbank.dto.account.CreateAccountRequest;
import com.bank.smartbank.exception.AccountNotFoundException;
import com.bank.smartbank.entity.Account;
import com.bank.smartbank.entity.AccountStatus;
import com.bank.smartbank.entity.AccountType;
import com.bank.smartbank.entity.User;
import com.bank.smartbank.exception.UserNotFoundException;
import com.bank.smartbank.repository.AccountRepository;
import com.bank.smartbank.repository.UserRepository;
import com.bank.smartbank.util.AccountNumberGenerator;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountService {

	private final AccountRepository accountRepository;
	private final UserRepository userRepository;
	private final AccountNumberGenerator accountNumberGenerator;

	public AccountService(AccountRepository accountRepository, UserRepository userRepository,
			AccountNumberGenerator accountNumberGenerator) {

		this.accountRepository = accountRepository;
		this.userRepository = userRepository;
		this.accountNumberGenerator = accountNumberGenerator;
	}

//	Create new account for user

	public AccountResponse createAccount(Long userId, CreateAccountRequest request) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

		String accountNumber = generateUniqueAccountNumber();

		Account account = new Account();
		account.setAccountNumber(accountNumber);
		account.setBalance(BigDecimal.ZERO);
		account.setType(AccountType.valueOf(request.getType()));
		account.setStatus(AccountStatus.ACTIVE);
		account.setUser(user);

		Account savedAccount = accountRepository.save(account);

		System.out.println("Account created :" + accountNumber);

		return new AccountResponse(savedAccount);
	}

//	Get all accounts for a user

	public List<AccountResponse> getUserAccounts(Long userId) {
		if (!userRepository.existsById(userId)) {
			throw new UserNotFoundException(userId);
		}

		List<Account> accounts = accountRepository.findByUserId(userId);

		return accounts.stream().map(AccountResponse::new).collect(Collectors.toList());
	}

//	Get account by account number

	public AccountResponse getAccountNumber(String accountNumber) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountNotFoundException("accountNumber", accountNumber));

		return new AccountResponse(account);
	}

//	 Get account entity (for internal use by other services)

	public Account getAccountEntity(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountNotFoundException("accountNumber", accountNumber));
	}

//	Check if user owns the account

	public boolean isAccountOwnedByUser(String accountNumber, long userId) {
		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new AccountNotFoundException("accounNumber", accountNumber));

		return account.getUser().getId().equals(userId);
	}

//	Get account balance

	public BigDecimal getAccountBalance(String accountNumber) {
		Account account = getAccountEntity(accountNumber);
		return account.getBalance();
	}

//	Update account balance (internal method)

	protected void updateBalance(Account account, BigDecimal newBalance) {
		account.setBalance(newBalance);
		accountRepository.save(account);
	}

//	------------------HELPER METHODS-----------------

//	Generate unique account number

	private String generateUniqueAccountNumber() {
		String accountNumber;
		do {
			accountNumber = accountNumberGenerator.generateAccountNumber();
		} while (accountRepository.existsByAccountNumber(accountNumber));

		return accountNumber;
	}
}
