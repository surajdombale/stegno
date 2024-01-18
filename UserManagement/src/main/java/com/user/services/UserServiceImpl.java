package com.user.services;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.entitites.ImageData;
import com.user.entitites.User;
import com.user.repositories.ImageDataRepo;
import com.user.repositories.UserRepository;
import com.user.util.Encryptor;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ImageDataRepo imgRepo;

	@Override
	public User saveUser(User user) throws Exception {
		if (!userRepo.findByEmail(user.getEmail()).isEmpty()) {
			System.out.println("hi" + userRepo.findByEmail(user.getEmail()));
			return null;
		}
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
	public String enableUser(String username) {
		User user = userRepo.findByEmail(username).get();
		user.setEnable(!user.isEnable());
		return userRepo.save(user).getUsername();
	}

	@Override
	public Map<String, Long> spamUser() {

		return imgRepo.findByBanIsTrue().stream()
				.collect(Collectors.groupingBy(ImageData::getUsername, Collectors.counting())).entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
						Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
	};

	@Override
	public boolean deleteSpamImage(Integer id) {
		try {
			imgRepo.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean deleteUser(String username) {

		return userRepo.deleteByEmail(username);
	}

	@Override
	public boolean banSpamImage(Integer id) {
		ImageData img = imgRepo.findById(id).get();
		img.setBan(!img.isBan());
		imgRepo.save(img);
		return img.isBan();
	}

}
