package com.app.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final String secretKey="BS695FBFMgEyRnfvp11VYDJq6uW2MqMtPsO2Z0usNlA=" ;
	
//	public JwtService() throws NoSuchAlgorithmException {
//		KeyGenerator key = KeyGenerator.getInstance("HmacSHA256");
//		SecretKey sk = key.generateKey();
//		secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//	}
	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", email);
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(email)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+1000*60*60*24))
				.and()
				.signWith(getKey())
				.compact();
				
	}

	private SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String extractUserName(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	
	public <T> T extractClaim(String token,Function<Claims,T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername())&& !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public String getEmailFromToken(String jwt) {
		jwt = jwt.substring(7);
		Claims claims = extractAllClaims(jwt);
		String email = String.valueOf(claims.get("email"));
		return email;
	}
}