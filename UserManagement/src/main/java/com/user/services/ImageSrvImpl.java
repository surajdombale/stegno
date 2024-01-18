package com.user.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.user.entitites.ImageData;
import com.user.entitites.User;
import com.user.entitites.UserEntry;
import com.user.repositories.ImageDataRepo;
import com.user.repositories.UserEntryRepo;
import com.user.repositories.UserRepository;
import com.user.util.Encryptor;
import com.user.util.Stegno;

@Service
public class ImageSrvImpl implements ImageSrv {
	@Autowired
	ImageDataRepo imgRepo;
	@Autowired
	UserEntryRepo userEntryRepo;
	@Autowired
	UserRepository userRepo;

	@Override
	public String retrieveTextFromImage(MultipartFile imageFile, String username) throws Exception {
		Integer id = Stegno.getText(imageFile);
		if (id == -1) {
			return "!!! Currupted Image !!!";
		}

		// First retrive the image id from image
		ImageData imgData = imgRepo.findById(id).get();
		if (imgData.isBan() || !imgData.isEnable()) {
			return "!!! Image is Banned !!!";
		}

		User user = userRepo.findByEmail(imgData.getUsername()).get();
		String key = user.getPin();
		if (key == null || !user.isEnabled()) {
			return "!!! Data Not Found !!!";
		}

		userEntryRepo.save(new UserEntry(username, id, LocalDateTime.now()));

		return Encryptor.decrypt(imgData.getImgText(), key);
	}

	@Override
	public ResponseEntity<byte[]> addText(MultipartFile imageFile, String text, String username) throws Exception {
		// Encrypt text and store it in user database using userId and add return image
		// id in image
		ImageData imgData = imgRepo
				.save(new ImageData(Encryptor.encrypt(text, userRepo.findByEmail(username).get().getPin()), username,
						true, LocalDateTime.now(), false));
		// Set appropriate headers for the response
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(Stegno.addText(imageFile, imgData.getId()), headers, HttpStatus.OK);
	}

	@Override
	public boolean enableImage(Integer id) {
		ImageData img = imgRepo.findById(id).get();
		img.setEnable(!img.isEnable());
		imgRepo.save(img);
		return !img.isEnable();
	}

	@Override
	public boolean addCheck(String username) {
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
		List<ImageData> imgData = imgRepo.findByUsernameAndTimeAfter(username, oneWeekAgo);

		return imgData.size() >= 6;
	}

	@Override
	public boolean seenCheck(String username) {
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
		List<UserEntry> userEntry = userEntryRepo.findByUsernameAndTimeAfter(username, oneWeekAgo);

		return userEntry.size() >= 6;
	}

}
