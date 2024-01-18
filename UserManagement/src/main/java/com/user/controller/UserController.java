package com.user.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
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
		return userService.enableUser(username);

	}

	// http://localhost:8080/user/enableimage
	@PostMapping("/banimage")
	public boolean banImage(@RequestParam("id") Integer id) {
		return userService.banSpamImage(id);

	}

	// http://localhost:8080/user/delete
	@PostMapping("/delete")
	public boolean deleteUser(@RequestParam("username") String username) {
		return userService.deleteUser(username);

	}

	// http://localhost:8080/user/spamuser
	@PostMapping("/spamuser")
	public Map<String, Long> spamUser() {
		return userService.spamUser();
	}

	// http://localhost:8080/user/deleteimage
	@PostMapping("/deleteimage")
	public boolean deleteImage(@RequestParam("id") Integer id) {
		return userService.deleteSpamImage(id);

	}

}
