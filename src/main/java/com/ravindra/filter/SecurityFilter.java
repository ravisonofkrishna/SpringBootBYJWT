package com.ravindra.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ravindra.util.JwtUtil;
@Component
public class SecurityFilter extends OncePerRequestFilter{
	@Autowired
	private JwtUtil util;
	@Autowired
	private UserDetailsService userDetailsService;
	@Override
	@Bean
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//Read token from Auth head
		String token=request.getHeader("Authorization");
		if(token!=null)
		{
			String username=util.getUsername(token);
			//username should not be empty, context path must be empty
			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
			{
				UserDetails usr=userDetailsService.loadUserByUsername(username);
				boolean isValid=util.validateToken(token, usr.getUsername());
				if(isValid)
				{
					UsernamePasswordAuthenticationToken authToken=
							new UsernamePasswordAuthenticationToken(username, usr.getPassword(),
									usr.getAuthorities());
					
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//final details store in SecurityContext with UserDetails(un,pwd)
				SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
		filterChain.doFilter(request, response);
	}
	

}
