package com.ravindra.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	@Value("${app.secret}")
	private String secret;
	
	public boolean validateToken(String token,String username)
	{
		String tokenUserName=getUsername(token);
		return (username.equals(tokenUserName) && !isTokenExp(token));
	}
	
	//validate Exp Date
	public Boolean isTokenExp(String token)
	{	Date expDate=getExpDate(token);
		return expDate.before(new Date(System.currentTimeMillis()));
	}
	
	
	//Read Subject/Usernamae
	public String getUsername(String token)
	{
		return getClaims(token).getSubject(); 
	}
	//Read Exp Date
	public Date getExpDate(String token)
	{
		return getClaims(token).getExpiration();
	}
	
	// Read Claims
	public Claims getClaims(String token)
	{
		return Jwts.parser()
				.setSigningKey(secret.getBytes())
				.parseClaimsJws(token)
				.getBody();
	}
	// To Generate Token
	public String generateToken(String subject)
	{
		return Jwts.builder()
				.setSubject("ravindrait")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15000000)))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact()
				;
	
	}
}
