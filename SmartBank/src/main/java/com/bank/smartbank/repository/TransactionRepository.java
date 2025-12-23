package com.bank.smartbank.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.smartbank.entity.Account;
import com.bank.smartbank.entity.Transaction;
import com.bank.smartbank.entity.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Optional<Transaction> findByTransactionRef(String transactionRef);

	List<Transaction> findByAccountOrderByCreatedAtDesc(Account account);

	List<Transaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);

	List<Transaction> findByAccountAndType(Account account, TransactionType type);

	@Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId "
			+ "AND t.createdAt BETWEEN :startDate AND :endDate " + "ORDER BY t.createdAt ASC")
	List<Transaction> findByAccountAndDateRange(@Param("accountId") Long accountId,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId " + "ORDER BY t.createdAt DESC")
	List<Transaction> findRecentTransactions(@Param("accountId") Long accountId);

	Long countByAccountAndType(Account account, TransactionType type);

	boolean existsByTransactionRef(String transactionRef);

}
