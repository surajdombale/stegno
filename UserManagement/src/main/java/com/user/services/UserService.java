package com.user.services;


import java.util.List;

import com.user.entitites.User;

public interface UserService {

	
	
	public User saveUser(User user) throws Exception;
	
	public List<User> getAllUser();

	public String enable(String username); 
	
	
}
