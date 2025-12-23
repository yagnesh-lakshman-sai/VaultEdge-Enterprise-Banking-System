package com.bank.smartbank.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.smartbank.entity.Account;
import com.bank.smartbank.entity.AccountStatus;
import com.bank.smartbank.entity.User;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByAccountNumber(String accountNumber);

	boolean existsByAccountNumber(String accountNumber);

	List<Account> findByUser(User user);

	List<Account> findByUserId(Long userId);

	List<Account> findByUserAndStatus(User user, AccountStatus status);

	boolean existsByBalanceGreaterThan(BigDecimal amount);

	@Query("SELECT a FROM Account a  WHERE a.user.id = :userId AND a.status = :status")
	List<Account> findActiveAccountsByUserId(@Param("userId") Long userId, @Param("status") AccountStatus status);

	@Query(value = "SELECT * FROM accounts WHERE balance > :minBalance", nativeQuery = true)
	List<Account> findRichAccounts(@Param("minBalance") BigDecimal minBalance);
}
