package com.ravindra.service;

import java.util.Optional;

import com.ravindra.model.User;

public interface IUserService {
	
	Integer saveUser(User user);
	Optional<User> findByUsername(String username);

}
