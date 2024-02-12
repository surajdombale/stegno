package com.user.services;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.user.entitites.UserEntry;
import com.user.repositories.ImageDataRepo;
import com.user.repositories.UserEntryRepo;
import com.user.repositories.UserRepository;
import com.user.util.Encryptor;
import com.user.util.Mail;
import com.user.util.PasswordGenerator;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ImageDataRepo imgRepo;
	@Autowired
	private UserEntryRepo entryRepo;

	@Override
	public User saveUser(User user) throws Exception {
		if (!userRepo.findByEmail(user.getEmail()).isEmpty()) {
			
			return null;
		}
//		String password=PasswordGenerator.password();
		user.setUserId(UUID.randomUUID().toString());
//		user.setPassword(passwordEncoder.encode(password));
		user.setPassword(passwordEncoder.encode("user"));
		user.setPin(Encryptor.generateSecretKey());	
		user.setSubDate(LocalDate.now().minusDays(29));
		user.setJoinDate(LocalDate.now());
		user.setEnable(true);
		User us=userRepo.save(user);
		if(us!=null) {
			
//			Mail.sendEmail(user.getEmail(), "your username : "+user.getEmail()+", your password is "+password, "new account opened");
		}
		return us;
	}

	@Override
	public List<User> getAllUser() {
		return userRepo.findAll();
	}

	@Override
	public String enableUser(String username) {
		User user = userRepo.findByEmail(username).get();
		user.setEnable(!user.isEnable());
		User us=userRepo.save(user);
		if(us!=null) {
			if(us.isEnable()) {
//			Mail.sendEmail(us.getEmail(), "your id is Enabled by Admin try login now", "account unbanned");
			}else {
//				Mail.sendEmail(us.getEmail(), "your id is Banned by Admin  Contact Admin", "account banned");
			}
		}
		return us.getUsername();
	}

	@Override
	public Map<String, Long> spamUser() {

		Map<String, Long> map = imgRepo.findByBanIsTrue().stream()
				.collect(Collectors.groupingBy(ImageData::getUsername, Collectors.counting())).entrySet().stream()
				.filter(entry -> entry.getValue() >= 6).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));
		for (User user : userRepo.findByEnableIsFalse()) {
			map.remove(user.getEmail());
		}

		return map;
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
		boolean result=false;
		try {
		userRepo.deleteById(userRepo.findByEmail(username).get().getUserId());
		result=true;
//		Mail.sendEmail(username, "your id is Deleted by Admin  Contact Admin", "account Deleted");
		}catch(Exception e) {
			
		}
		return result;
	}

	@Override
	public boolean banSpamImage(Integer id) {
		ImageData img = imgRepo.findById(id).get();
		img.setBan(!img.isBan());

		if(imgRepo.save(img)!=null) {
			if(!img.isBan()) {
//				Mail.sendEmail(img.getUsername(), "your image id : "+img.getId() +" is unbanned by Admin ", "image unbanned");
				}else {
//					Mail.sendEmail(img.getUsername(), "your image id : "+img.getId() +"  is Banned by Admin  Contact Admin", "image banned");
				}	
		}
		return img.isBan();
	}

	@Override
	public List<ImageData> getAllImage() {
		// TODO Auto-generated method stub
		return imgRepo.findAll();
	}

	@Override
	public User getUserByEmail(String username) {
		User us=userRepo.findByEmail(username).get();
		us.setPassword("");
		us.setPin("");

		return us;
	}

	@Override
	public User registerUser(User user) throws Exception {
		if (!userRepo.findByEmail(user.getEmail()).isEmpty()) {
			
			return null;
		}
		
		user.setUserId(UUID.randomUUID().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setPin(Encryptor.generateSecretKey());	
		user.setSubDate(LocalDate.now().minusDays(29));
		user.setJoinDate(LocalDate.now());
		user.setEnable(true);
		user.setRole("ADMIN");
		User us=userRepo.save(user);
		if(us!=null) {
			
//			Mail.sendEmail(user.getEmail(), "your username : "+user.getEmail()+", your password is "+user.getPassword(), "new account opened");
		}
		return us;
	}

	@Override
	public boolean editUser(String name, String mail, String gmail,String role) {
	        User user=userRepo.findByEmail(gmail).get();
	        if(!gmail.matches(mail)) {
	        	List<ImageData> imgList=imgRepo.findByUsername(gmail);
	        	List<ImageData> setimg=new ArrayList<>();
	        	for(ImageData img:imgList) {
	        		img.setUsername(mail);
	        		setimg.add(img);
	        	}
	        	imgRepo.saveAllAndFlush(setimg);
	        	List<UserEntry> userentry=entryRepo.findByUsername(gmail);
	        	List<UserEntry> entry=new ArrayList<>();
	        	for(UserEntry ent:userentry) {
	        		ent.setUsername(mail);
	        		entry.add(ent);
	        	}
	        	entryRepo.saveAllAndFlush(entry);
//	        	Mail.sendEmail(gmail, "your username is Changed to  : "+mail, "Email Changed");
	    		
	        }
	        user.setEmail(mail);
	        user.setFullName(name);
	        if(role!="NULL") {
	        	user.setRole(role);
	        }
	       User us= userRepo.save(user);
	        if(us!=null) {
	        	return true;
	        }
	        
		return false;
	}

	@Override
	public List<String> allEmail() {
		List<String> list=new ArrayList<>();
		List<User> userList=userRepo.findAll();
		for(User us:userList) {
			list.add(us.getEmail());
		}
		return list;
	}

	@Override
	public String EditPassword(String username, String newPassword) {
		try{User user=userRepo.findByEmail(username).get();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepo.saveAndFlush(user);
		return username;
		}
		catch(Exception e) {
			
		}
		return null;
	}

	@Override
	public String subscribe(String usrname) {
	User user=	userRepo.findByEmail(usrname).get();
	user.setSubDate(LocalDate.now());
	try {
		userRepo.saveAndFlush(user);
		return ""+LocalDate.now().plusDays(28);
	}catch(Exception e) {
		
	}
		
		return null;
	}

}
