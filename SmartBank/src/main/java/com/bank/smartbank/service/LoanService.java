package com.bank.smartbank.service;

import com.bank.smartbank.dto.loan.*;
import com.bank.smartbank.entity.*;
import com.bank.smartbank.exception.*;
import com.bank.smartbank.repository.*;
import com.bank.smartbank.util.Constants;
import com.bank.smartbank.util.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanService {

	private final LoanRepository loanRepository;
	private final UserRepository userRepository;
	private final EmailService emailService;

	public LoanService(LoanRepository loanRepository, UserRepository userRepository, EmailService emailService) {
		this.loanRepository = loanRepository;
		this.userRepository = userRepository;
		this.emailService = emailService;
	}

	public LoanResponse applyForLoan(Long userId, LoanRequest request) {

		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

		if (loanRepository.hasActiveLoan(userId)) {
			throw new ApplicationException(
					"You already have an active loan. Please repay it before applying for a new one.");
		}

		if (request.getAmount().compareTo(Constants.MINIMUM_LOAN_AMOUNT) < 0) {
			throw new ApplicationException("Minimum loan amount is ₹" + Constants.MINIMUM_LOAN_AMOUNT);
		}

		if (request.getAmount().compareTo(Constants.MAXIMUM_LOAN_AMOUNT) > 0) {
			throw new ApplicationException("Maximum loan amount is ₹" + Constants.MAXIMUM_LOAN_AMOUNT);
		}

		Loan loan = new Loan();
		loan.setUser(user);
		loan.setAmount(request.getAmount());
		loan.setInterestRate(Constants.DEFAULT_INTEREST_RATE); // 12.5%
		loan.setTenureMonths(request.getTenureMonths());
		loan.setPurpose(request.getPurpose());
		loan.setStatus(LoanStatus.PENDING);

		BigDecimal emiAmount = calculateEMI(request.getAmount(), Constants.DEFAULT_INTEREST_RATE,
				request.getTenureMonths());
		loan.setEmiAmount(emiAmount);

		Loan savedLoan = loanRepository.save(loan);

		System.out.println(" Loan application submitted: " + savedLoan.getLoanNumber());
		System.out.println(" Amount: ₹" + savedLoan.getAmount());
		System.out.println(" EMI: ₹" + savedLoan.getEmiAmount());

		return new LoanResponse(savedLoan);
	}

	public List<LoanResponse> getUserLoans(Long userId) {

		if (!userRepository.existsById(userId)) {
			throw new UserNotFoundException(userId);
		}

		List<Loan> loans = loanRepository.findByUserIdOrderByAppliedDateDesc(userId);

		return loans.stream().map(LoanResponse::new).collect(Collectors.toList());
	}

	public LoanResponse getLoanById(Long loanId) {
		Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));

		return new LoanResponse(loan);
	}

	public LoanResponse getLoanByNumber(String loanNumber) {
		Loan loan = loanRepository.findByLoanNumber(loanNumber)
				.orElseThrow(() -> new LoanNotFoundException("loanNumber", loanNumber));

		return new LoanResponse(loan);
	}

	public LoanResponse approveLoan(Long loanId, Long adminId, LoanApprovalRequest request) {

		Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));

		if (loan.getStatus() != LoanStatus.PENDING && loan.getStatus() != LoanStatus.UNDER_REVIEW) {
			throw new ApplicationException("Loan has already been processed. Current status: " + loan.getStatus());
		}

		loan.setApprovedBy(adminId);
		loan.setApprovedDate(LocalDateTime.now());

		if (request.getApproved()) {

			loan.setStatus(LoanStatus.APPROVED);

			System.out.println(" Loan APPROVED: " + loan.getLoanNumber());
			System.out.println("  Amount: ₹" + loan.getAmount());
			System.out.println("  Approved by Admin ID: " + adminId);

			emailService.sendLoanApprovalEmail(loan.getUser().getEmail(), loan.getLoanNumber(),
					loan.getAmount().toString());

		} else {

			loan.setStatus(LoanStatus.REJECTED);
			loan.setRejectionReason(request.getRemarks() != null ? request.getRemarks()
					: "Loan application did not meet approval criteria");

			System.out.println("Loan REJECTED: " + loan.getLoanNumber());
			System.out.println(" Reason: " + loan.getRejectionReason());

			emailService.sendEmail(loan.getUser().getEmail(), "Loan Application Status",
					"Dear " + loan.getUser().getFullName() + ",\n\n" + "Your loan application (Loan Number: "
							+ loan.getLoanNumber() + ") has been reviewed.\n\n"
							+ "Unfortunately, we are unable to approve your loan at this time.\n" + "Reason: "
							+ loan.getRejectionReason() + "\n\n"
							+ "You may reapply after addressing the concerns mentioned above.\n\n" + "Best regards,\n"
							+ "Smart Bank Team");
		}

		Loan savedLoan = loanRepository.save(loan);

		return new LoanResponse(savedLoan);
	}

	public List<LoanResponse> getPendingLoans() {
		List<LoanStatus> pendingStatuses = Arrays.asList(LoanStatus.PENDING, LoanStatus.UNDER_REVIEW);

		List<Loan> loans = loanRepository.findByStatusIn(pendingStatuses);

		return loans.stream().map(LoanResponse::new).collect(Collectors.toList());
	}

	public List<LoanResponse> getLoansByStatus(LoanStatus status) {
		List<Loan> loans = loanRepository.findByStatusOrderByAppliedDateDesc(status);

		return loans.stream().map(LoanResponse::new).collect(Collectors.toList());
	}

	public LoanResponse markUnderReview(Long loanId) {
		Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));

		if (loan.getStatus() != LoanStatus.PENDING) {
			throw new ApplicationException("Only pending loans can be marked as under review");
		}

		loan.setStatus(LoanStatus.UNDER_REVIEW);
		Loan savedLoan = loanRepository.save(loan);

		System.out.println(" Loan marked as UNDER_REVIEW: " + loan.getLoanNumber());

		return new LoanResponse(savedLoan);
	}

	private BigDecimal calculateEMI(BigDecimal principal, BigDecimal annualRate, int tenureMonths) {

		if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
			return principal.divide(new BigDecimal(tenureMonths), 2, RoundingMode.HALF_UP);
		}

		BigDecimal monthlyRate = annualRate.divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP)
				.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);

		BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);

		BigDecimal power = onePlusR.pow(tenureMonths);

		BigDecimal numerator = principal.multiply(monthlyRate).multiply(power);

		BigDecimal denominator = power.subtract(BigDecimal.ONE);

		BigDecimal emi = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

		return emi;
	}

	public boolean isLoanOwnedByUser(Long loanId, Long userId) {
		Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new LoanNotFoundException(loanId));

		return loan.getUser().getId().equals(userId);
	}

}