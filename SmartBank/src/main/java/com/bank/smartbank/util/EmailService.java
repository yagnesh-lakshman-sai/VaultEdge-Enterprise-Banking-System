package com.bank.smartbank.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	@Value("${spring.mail.username:noreply@smartbank.com}")
	private String fromEmail;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;

	}

	@Async
	public void sendEmail(String to, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);

			mailSender.send(message);
			System.out.println(" Email sent to: " + to);
		} catch (Exception e) {
			System.out.println("Failed to send email to :" + to);
			e.printStackTrace();
		}
	}

	@Async
	public void sendOtpEmail(String to, String otp) {
		String subject = Constants.EMAIL_SUBJECT_OTP;
		String text = buildOtpEmailBody(otp);
		sendEmail(to, subject, text);
	}

	@Async
	public void sendWelcomeEmail(String to, String name) {
		String subject = Constants.EMAIL_SUBJECT_WELCOME;
		String text = buildWelcomeEmailBody(name);
		sendEmail(to, subject, text);
	}

	@Async
	public void sendTransferConfirmationEmail(String to, String transactionRef, String amount) {
		String subject = Constants.EMAIL_SUBJECT_TRANSFER;
		String text = buildTransferEmailBody(transactionRef, amount);
		sendEmail(to, subject, text);
	}

	@Async
	public void sendLoanApprovalEmail(String to, String loanNumber, String amount) {
		String subject = Constants.EMAIL_SUBJECT_LOAN_APPROVED;
		String text = buildLoanApprovalEmailBody(loanNumber, amount);
		sendEmail(to, subject, text);
	}

	private String buildOtpEmailBody(String otp) {
		return String.format("""
				Dear Customer,

				Your OTP code for Smart Bank verification is:

				%s

				This code will expire in %d minutes.

				If you did not request this code, please ignore this email.

				Best regards,
				Smart Bank Team
				""", otp, Constants.OTP_VALIDITY_MINUTES);
	}

	private String buildWelcomeEmailBody(String name) {
		return String.format("""
				Dear %s,

				Welcome to Smart Bank!

				Your account has been successfully created. You can now:
				- Create savings and current accounts
				- Transfer funds securely
				- Apply for loans
				- Track your transactions

				Thank you for choosing Smart Bank.

				Best regards,
				Smart Bank Team
				""", name);
	}

	private String buildTransferEmailBody(String transactionRef, String amount) {
		return String.format("""
				Dear Customer,

				Your transfer has been completed successfully.

				Transaction Reference: %s
				Amount: ₹%s

				If you did not authorize this transaction, please contact us immediately.

				Best regards,
				Smart Bank Team
				""", transactionRef, amount);
	}

	private String buildLoanApprovalEmailBody(String loanNumber, String amount) {
		return String.format("""
				Dear Customer,

				Congratulations! Your loan application has been approved.

				Loan Number: %s
				Approved Amount: ₹%s

				The amount will be credited to your account within 24 hours.

				Best regards,
				Smart Bank Team
				""", loanNumber, amount);
	}
}
