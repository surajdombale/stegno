package com.user.entitites;

import java.time.LocalDate;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserData")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;


	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	@Id
	private String userId;
	@Column
	private String password;
	@Column
	private String fullName;
	@Column
	private String pin;
	@Column
	private String role;
	@Column
	private LocalDate subDate;
	@Column
	private LocalDate joinDate;

	public LocalDate getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}

	public LocalDate getSubDate() {
		return subDate;
	}

	public void setSubDate(LocalDate subDate) {
		this.subDate = subDate;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	private String email;
	private boolean enable;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		if (getRole().contains("ADMIN")) {
			authorities.add(new SimpleGrantedAuthority("ADMIN"));
			authorities.add(new SimpleGrantedAuthority("USER"));
		} else {
			authorities.add(new SimpleGrantedAuthority("USER"));
		}
		
		return authorities;

	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enable;
	}

	public boolean isEnable() {
		return enable;
	}





}