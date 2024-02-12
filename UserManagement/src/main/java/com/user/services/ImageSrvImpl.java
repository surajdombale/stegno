package com.user.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
		System.out.println(id);
		if (id == -1) {
			return "!!! Currupted Image contact Sender !!!";
		}

		// First retrive the image id from image
		ImageData imgData = imgRepo.findById(id).get();
		if (imgData.isBan()) {
			return "!!! Image Id no : " + id + " is Banned by Admin !!!";
		}
		if (!imgData.isEnable()) {
			return "!!! Image Id no : " + id + " is Disable by User !!!";
		}

		User user = userRepo.findByEmail(imgData.getUsername()).get();
		String key = user.getPin();
		if (key == null || !user.isEnabled()) {
			return "!!! Owner of Image Id no : " + id + " is Banned Or Deleted by Admin !!!";
		}

		userEntryRepo.save(new UserEntry(username, id, LocalDateTime.now()));

		return " Image Id : " + id + " text : " + Encryptor.decrypt(imgData.getImgText(), key);
	}

	@Override
	public String addText(MultipartFile imageFile, String text, String username) throws Exception {
		// Encrypt text and store it in user database using userId and add return image
		// id in image
		ImageData imgData = imgRepo
				.save(new ImageData(Encryptor.encrypt(text, userRepo.findByEmail(username).get().getPin()), username,
						true, LocalDateTime.now(), false, null));
		byte[] image = Stegno.addText(imageFile, imgData.getId());
		imgData.setImage(image);
		imgRepo.save(imgData);
		// Set appropriate headers for the response

		return username + "&" + imgData.getId();
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

	@Override
	public List<ImageData> getAllImageByUser(String username) {

		return imgRepo.findByUsername(username);
	}

	@Override
	public boolean disableUser(String username) {
		User user = userRepo.findByEmail(username).get();
		user.setEnable(false);
		return userRepo.save(user) != null;
	}

	@Override
	public boolean enableUser(String username) {
		User user = userRepo.findByEmail(username).get();
		user.setEnable(true);
		return userRepo.save(user) != null;
	}

	@Override
	public List<UserEntry> getUserEntry(Integer imageId) {
		// TODO Auto-generated method stub
		return userEntryRepo.findByImageId(imageId);
	}

	@Override
	public byte[] getImageById(String id) {
		String[] str = id.split("&");
		String user = str[0];
		Integer iid = Integer.parseInt(str[1]);
		ImageData image = imgRepo.findById(iid).get();
		if (image.getUsername().contentEquals(user)) {
			return image.getImage();
		}
		return null;
	}

	@Override
	public boolean deleteImage(Integer id,String username) {
		try {
			if(imgRepo.findById(id).get().getUsername().matches(username)) {
			imgRepo.deleteById(id);
			return true;
			}else {
			return false;
			}
		}catch(Exception e) {
			
		}
		
		return false;
	}

}
