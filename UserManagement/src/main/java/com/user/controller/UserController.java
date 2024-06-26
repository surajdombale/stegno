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

import com.user.entitites.ImageData;
import com.user.entitites.User;
import com.user.services.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:3000")
//@CrossOrigin("https://imageencryptorpro.up.railway.app")
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
	@GetMapping("/save")
	public User saveUser(@RequestParam("username") String username, @RequestParam("role") String role,
			@RequestParam("fullName") String fullName) throws Exception {
		User user = new User();
		user.setFullName(fullName);
		user.setEmail(username);
		user.setRole(role);
		System.out.println(user);
		return userService.saveUser(user);
	}

	// http://localhost:8080/user/allusers
	@GetMapping("/allusers")
	public List<User> getAllUserList() {

		return userService.getAllUser();
	}

	// http://localhost:8080/user/banuser
	@GetMapping("/banuser")
	public String enableUser(@RequestParam("username") String username) {
		return userService.enableUser(username);

	}

	// http://localhost:8080/user/banimage
	@GetMapping("/banimage")
	public boolean banImage(@RequestParam("id") Integer id) {
		return userService.banSpamImage(id);

	}

	// http://localhost:8080/user/delete
	@GetMapping("/delete")
	public boolean deleteUser(@RequestParam("username") String username) {
		return userService.deleteUser(username);

	}

	// http://localhost:8080/user/spamuser
	@GetMapping("/spamuser")
	public Map<String, Long> spamUser() {
		return userService.spamUser();
	}

	// http://localhost:8080/user/deleteimage
	@GetMapping("/deleteimage")
	public boolean deleteImage(@RequestParam("id") Integer id) {
		return userService.deleteSpamImage(id);

	}

	// http://localhost:8080/user/getallimagex
	@GetMapping("/getallimage")
	public List<ImageData> getAllImage() {

		return userService.getAllImage();
	}

	// http://localhost:8080/user/edituser
	@GetMapping("/edituser")
	public boolean editUser(@RequestParam("mail") String mail, @RequestParam("email") String email,
			@RequestParam("role") String role, @RequestParam("name") String name) {

		return userService.editUser(name, mail, email, role);
	}

	// http://localhost:8080/user/getuser
	@GetMapping("/getuser")
	public User getUser(@RequestParam("username") String username) {
		User user = userService.getUserByEmail(username);
		user.setPassword("");
		user.setPin("");
		return user;
	}
}
