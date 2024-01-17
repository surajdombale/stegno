package com.user.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.entitites.User;

import com.user.repositories.UserRepository;
import com.user.util.Encryptor;
@Service
public class UserServiceImpl implements UserService  {

	@Autowired
	public  UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public User saveUser(User user) throws Exception {
		user.setUserId(UUID.randomUUID().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setPin(Encryptor.generateSecretKey());
		return userRepo.save(user);
	}



	@Override
	public List<User> getAllUser() {
		return userRepo.findAll();
	}



	@Override
	public String enable(String username) {
		User user=userRepo.findByEmail(username).get();
		user.setEnable(!user.isEnable());
		return userRepo.save(user).getUsername(); 
	}






	

}

	


