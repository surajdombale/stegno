package com.user.entitites;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table
public class ImageData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Lob
	@Column(length = 52428800)
	private byte[] image;	
	

	@Column
	private String imgText;
	@Column
	private String username;
	@Column
	private boolean enable;
	@Column
	private LocalDateTime time;
	@Column
	private boolean ban;

	public boolean isBan() {
		return ban;
	}

	public void setBan(boolean ban) {
		this.ban = ban;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public ImageData() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImgText() {
		return imgText;
	}

	public void setImgText(String imgText) {
		this.imgText = imgText;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	public ImageData(String imgText, String username, boolean enable, LocalDateTime time, boolean ban,byte[] image) {
		super();
		this.imgText = imgText;
		this.username = username;
		this.enable = enable;
		this.time = time;
		this.ban = ban;
		this.image = image;
	}

}