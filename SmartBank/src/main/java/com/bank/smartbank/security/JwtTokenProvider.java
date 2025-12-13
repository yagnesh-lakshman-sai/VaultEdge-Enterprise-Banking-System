package com.bank.smartbank.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;
import com.bank.smartbank.util.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final SecretKey secretKey = Keys.hmacShaKeyFor(
		Constants.JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	
//	Generate JWT token for authenticated user
	public String generateToken(Long userId, String email, String role) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + Constants.JWT_EXPIRATION_MS);
		
		return Jwts.builder()
				.subject(String.valueOf(userId))
				.claim(email, email)
				.claim(role, role)
				.issuedAt(now)
				.expiration(expiryDate)
				.signWith(secretKey)
				.compact();
	}
	
//	Extract user ID from JWT token
	
	public Long getUserIdFromToken(String token) {
		Claims claims = parseClaims(token);
		return Long.parseLong(claims.getSubject());
	}
	
//	Extract email from JWT token
	
	public String getEmailFromToken(String token) {
		Claims claims = parseClaims(token);
		return claims.get("emails", String.class);
	}
	
//	Extract role from JWT token
	
	public String getRoleFromToken(String token) {
		Claims claims = parseClaims(token);
		return claims.get("role", String.class);
	}
	
//	Validate JWT token
	
	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		}catch (JwtException  | IllegalArgumentException e) {
			System.out.println("Invalid JWT token: " +e.getMessage());
			return false;	
		}
	}
	
//	Parse JWT token and extract claims
	
	private Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
