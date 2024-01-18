package com.user.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageSrv {
	public String retrieveTextFromImage(MultipartFile imageFile, String username) throws Exception;

	public ResponseEntity<byte[]> addText(MultipartFile imageFile, String text, String username) throws Exception;

	public boolean enableImage(Integer id);

	public boolean addCheck(String username);

	public boolean seenCheck(String username);

}
