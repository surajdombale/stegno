package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.user.services.ImageSrv;

@RestController
@RequestMapping("/image")
@CrossOrigin
public class ImageController {
	@Autowired
	private ImageSrv imgSrv;

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
	
	// http://localhost:8080/user/enableimage
		@PostMapping("/enableimage")
		public boolean enableImage(@RequestParam("username") Integer id) {
			return imgSrv.enableImage(id);
			
		}

}
