package com.user.controller;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.user.config.TokenPoint;
import com.user.entitites.User;
import com.user.model.TokenRequest;
import com.user.model.TokenResponse;
import com.user.services.ImageSrv;
import com.user.services.UserService;
import com.user.util.Mail;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:3000")
//@CrossOrigin(origins = { "https://imageencryptorpro.up.railway.app" })
public class UserAuth {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager manager;
	@Autowired
	private TokenPoint point;

	@Autowired
	private UserService userService;
	@Autowired
	private ImageSrv imgSrv;

	// http://localhost:8080/auth/login
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {

		this.doAuthenticate(tokenRequest.getUsername(), tokenRequest.getPassword());

		UserDetails userDetails = userDetailsService.loadUserByUsername(tokenRequest.getUsername());
		String token = this.point.generateToken(userDetails);
		User user = userService.getUserByEmail(tokenRequest.getUsername());
		TokenResponse response = new TokenResponse();
		response.setToken(token);
		long daysDifference = ChronoUnit.DAYS.between(user.getSubDate(), LocalDate.now());
		response.setUserName(tokenRequest.getUsername());
		response.setRole(user.getRole());
		response.setSubDate(user.getSubDate().plusDays(28));
		response.setSub(daysDifference < 28);
		
		if(user.getRole().contentEquals("ADMIN")){
			
			response.setSub(true);
		}
		
		response.setJoinDate(user.getJoinDate());
		response.setFullName(user.getFullName());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void doAuthenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			manager.authenticate(authentication);

		} catch (BadCredentialsException e) {
			throw new BadCredentialsException(" Invalid Username or Password  !!");
		}

	}

	@ExceptionHandler(BadCredentialsException.class)
	public String exceptionHandler() {
		return "Credentials Invalid !!";
	}

	// http://localhost:8080/auth/save
	@GetMapping("/save")
	public User saveUser(@RequestParam("email") String username, @RequestParam("fullName") String fullName,
			@RequestParam("password") String password) throws Exception {
		User user = new User();
		user.setEmail(username);
		user.setFullName(fullName);
		user.setPassword(password);
		return userService.registerUser(user);
	}

	

	@GetMapping("/status")
	public String status() {

		return "Running";
	}
	@GetMapping("/getusers")
	public List<String> getUsers() {

		return userService.allEmail();
	}

	@GetMapping("/sendmail")
	public Boolean sendMail(@RequestParam("to") String to, @RequestParam("msg") String msg,
			@RequestParam("subject") String subject) {
		return Mail.sendEmail(to, msg, subject);

	}

	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable String id) {
		byte[] image = imgSrv.getImageById(id);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(image);
	}

}
