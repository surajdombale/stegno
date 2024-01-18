package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.user.config.TokenPoint;
import com.user.entitites.User;
import com.user.model.TokenRequest;
import com.user.model.TokenResponse;
import com.user.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class UserAuth {
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager manager;
	@Autowired
	private TokenPoint point;

	@Autowired
	private UserService userService;

	// http://localhost:8080/auth/login
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {

		this.doAuthenticate(tokenRequest.getUsername(), tokenRequest.getPassword());

		UserDetails userDetails = userDetailsService.loadUserByUsername(tokenRequest.getUsername());
		String token = this.point.generateToken(userDetails);

		TokenResponse response = new TokenResponse();
		response.setToken(token);
		response.setUserName(userDetails.getUsername());
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
	@PostMapping("/save")
	public User saveUser(@RequestBody User user) throws Exception {

		return userService.saveUser(user);
	}

}
