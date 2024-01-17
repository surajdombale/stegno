package com.user.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.user.entitites.User;
import com.user.services.UserService;
import com.user.util.Mail;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;

	// http://localhost:8080/user/status
	@GetMapping("/status")
	public String status() {
		System.out.println("Running");
		return "Running";
	}

	// http://localhost:8080/user/currentuser
	@GetMapping("/currentuser")
	public String currentUser(Principal principle) {

		return principle.getName();
	}

	// http://localhost:8080/user/save
	@PostMapping("/save")
	public User saveUser(@RequestBody User user) throws Exception {

		return userService.saveUser(user);
	}

	// http://localhost:8080/user/allusers
	@GetMapping("/allusers")
	public List<User> getAllUserList() {

		return userService.getAllUser();
	}

	// http://localhost:8080/user/enable
	@PostMapping("/enable")
	public String enableUser(@RequestParam("username") String username) {
		return userService.enable(username);

	}

	// http://localhost:8080/user/sendmail
	@PostMapping("/sendmail")
	public Boolean sendMail(@RequestParam("to") String to, @RequestParam("msg") String msg,
			@RequestParam("subject") String subject) {
		return Mail.sendEmail(to, msg, subject);

	}

	

}
