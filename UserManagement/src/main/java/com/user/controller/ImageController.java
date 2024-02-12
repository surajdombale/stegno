package com.user.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.user.entitites.ImageData;
import com.user.entitites.User;
import com.user.entitites.UserEntry;
import com.user.services.ImageSrv;
import com.user.services.UserService;
import com.user.util.Mail;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "https://imageencryptorpro.up.railway.app", methods = { RequestMethod.POST, RequestMethod.GET })
public class ImageController {
	@Autowired
	private ImageSrv imgSrv;
	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager manager;

	// http://localhost:8080/image/addtext
	@PostMapping("/addtext")
	public String addTextToImage(@RequestParam("text") String text, @RequestParam("image") MultipartFile imageFile,
			@RequestParam("username") String username) throws Exception {

		return imgSrv.addText(imageFile, text, username);

	}

	// http://localhost:8080/image/gettext
	@PostMapping("/gettext")
	public String getTextFromImage(@RequestParam("image") MultipartFile imageFile,
			@RequestParam("username") String username) throws Exception {
		return imgSrv.retrieveTextFromImage(imageFile, username);

	}

	// http://localhost:8080/image/enableimage
	@GetMapping("/enableimage")
	public boolean enableImage(@RequestParam("id") Integer id) {
		return imgSrv.enableImage(id);

	}

	// http://localhost:8080/image/addcheck
	@GetMapping("/addcheck")
	public boolean addCheck(@RequestParam("username") String username) {
		return imgSrv.addCheck(username);

	}

	// http://localhost:8080/image/seencheck
	@GetMapping("/seencheck")
	public boolean seenCheck(@RequestParam("username") String username) {
		return imgSrv.seenCheck(username);

	}

	// http://localhost:8080/image/getall
	@GetMapping("/getall")
	public List<ImageData> getAllImageByUser(@RequestParam("username") String username) {

		return imgSrv.getAllImageByUser(username);
	}

	// http://localhost:8080/image/disable
	@GetMapping("/disable")
	public boolean disableUser(@RequestParam("username") String username) {
		return imgSrv.disableUser(username);

	}

	// http://localhost:8080/image/enable
	@GetMapping("/enable")
	public boolean enableUser(@RequestParam("username") String username) {
		return imgSrv.enableUser(username);

	}

	@GetMapping("/edituser")
	public boolean editUser(@RequestParam("mail") String mail, @RequestParam("email") String email,
			@RequestParam("role") String role, @RequestParam("name") String name) {

		return userService.editUser(name, mail, email, "NULL");
	}

	@GetMapping("/getentry")
	public List<UserEntry> getEntry(@RequestParam("id") String id) {

		return imgSrv.getUserEntry(Integer.parseInt(id));
	}

	@GetMapping("/editpass")
	public String EditPassword(@RequestParam("username") String username,
			@RequestParam("oldpassword") String oldPassword, @RequestParam("newpassword") String newPassword) {
		this.doAuthenticate(username, oldPassword);
		return userService.EditPassword(username, newPassword);
	}

	// http://localhost:8080/image/delete
	@GetMapping("/delete")
	public boolean deleteImage(@RequestParam("id") Integer id, @RequestParam("username") String username) {

		return imgSrv.deleteImage(id, username);

	}

	// http://localhost:8080/image/sub
	@GetMapping("/sub")
	public String subscribe(@RequestParam("username") String username) {

		return userService.subscribe(username);

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
}
