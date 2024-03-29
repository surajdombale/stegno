package com.user.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.user.entitites.ImageData;
import com.user.entitites.UserEntry;

public interface ImageSrv {
	public String retrieveTextFromImage(MultipartFile imageFile, String username) throws Exception;

	public String addText(MultipartFile imageFile, String text, String username) throws Exception;

	public boolean enableImage(Integer id);
	public byte[] getImageById(String id);

	public boolean addCheck(String username);

	public boolean seenCheck(String username);
	public List<ImageData> getAllImageByUser(String username);
	public boolean disableUser(String username);
	public boolean enableUser(String username);
	public List<UserEntry> getUserEntry(Integer imageId);
	public boolean deleteImage(Integer id,String username);

}
