package com.user.services;

import java.util.List;
import java.util.Map;

import com.user.entitites.User;

public interface UserService {

	public User saveUser(User user) throws Exception;

	public List<User> getAllUser();

	public String enableUser(String username);

	public Map<String, Long> spamUser();

	public boolean banSpamImage(Integer id);

	public boolean deleteSpamImage(Integer id);

	public boolean deleteUser(String username);

}
