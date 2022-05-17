package com.ravindra.service.Impl;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ravindra.model.User;
import com.ravindra.repo.UserRepository;
import com.ravindra.service.IUserService;
@Service
public class UserServiceImpl implements IUserService,UserDetailsService {
	@Autowired
	private UserRepository repo;
	@Autowired
	private BCryptPasswordEncoder pwdEncoder; 
	@Override
	public Integer saveUser(User user) {
		//EncodePassword
		user.setPassword(pwdEncoder.encode(user.getPassword()));
		
		return repo.save(user).getId();
		
		
	}
	//get user by username
	@Override
	public Optional<User> findByUsername(String username) {
		// TODO Auto-generated method stub
		return repo.findByUsername(username);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> opt=findByUsername(username);
		if(opt.isEmpty())throw new UsernameNotFoundException("User not exist");
		User user = opt.get();
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), user.getRoles().stream()
				.map(role->new SimpleGrantedAuthority(role))
				.collect(Collectors.toList()));
	}

}
