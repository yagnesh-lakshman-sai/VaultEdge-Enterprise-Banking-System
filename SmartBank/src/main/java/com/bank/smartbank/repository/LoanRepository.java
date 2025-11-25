package com.bank.smartbank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bank.smartbank.entity.Loan;
import com.bank.smartbank.entity.LoanStatus;
import com.bank.smartbank.entity.User;

public interface LoanRepository extends JpaRepository<Loan, Long>{

	Optional<Loan> findByLoanNumber(String loanNumber);
	
	List<Loan> findByUserOrderByAppliedDateDesc(User user);
	
	List<Loan> findByUserIdOrderByAppliedDateDesc(Long UserId);
	
	List<Loan> findByStatusOrderByAppliedDateDesc(LoanStatus status);
	
	List<Loan> findByStatusIn(List<LoanStatus> statuses);
	
	@Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END"+
	        "FROM Loan l WHERE l.user.id = : userId" +
			"AND l.status IN ('Approved', 'DISBURSED', 'ACTIVE')")
	boolean hasActiveLoan(Long userId);
	
	Long countByStatus(LoanStatus status);
	
	List<Loan> findByApprovedBy(Loan adminId);
	
	
}
