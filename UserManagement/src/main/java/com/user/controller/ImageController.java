package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.user.entitites.User;
import com.user.services.ImageSrv;
import com.user.services.UserService;
import com.user.util.Mail;

@RestController
@RequestMapping("/image")
@CrossOrigin
public class ImageController {
	@Autowired
	private ImageSrv imgSrv;
	@Autowired
	private UserService userService;

	// http://localhost:8080/image/addtext
	@PostMapping("/addtext")
	public ResponseEntity<byte[]> addTextToImage(@RequestParam("image") MultipartFile imageFile,
			@RequestParam("text") String text, @RequestParam("username") String username) throws Exception {
		return imgSrv.addText(imageFile, text, username);

	}

	// http://localhost:8080/image/gettext
	@PostMapping("/gettext")
	public String getTextFromImage(@RequestParam("image") MultipartFile imageFile,
			@RequestParam("username") String username) throws Exception {
		return imgSrv.retrieveTextFromImage(imageFile, username);

	}

	// http://localhost:8080/image/enableimage
	@PostMapping("/enableimage")
	public boolean enableImage(@RequestParam("username") Integer id) {
		return imgSrv.enableImage(id);

	}

	// http://localhost:8080/image/sendmail
	@PostMapping("/sendmail")
	public Boolean sendMail(@RequestParam("to") String to, @RequestParam("msg") String msg,
			@RequestParam("subject") String subject) {
		return Mail.sendEmail(to, msg, subject);

	}

	// http://localhost:8080/image/addcheck
	@PostMapping("/addcheck")
	public boolean addCheck(@RequestParam("username") String username) {
		return imgSrv.addCheck(username);

	}

	// http://localhost:8080/image/seencheck
	@PostMapping("/seencheck")
	public boolean seenCheck(@RequestParam("username") String username) {
		return imgSrv.seenCheck(username);

	}

	// http://localhost:8080/image/save
	@PostMapping("/save")
	public User saveUser(@RequestBody User user) throws Exception {
		user.setRole("USER");

		return userService.saveUser(user);
	}

}
