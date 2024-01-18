package com.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.entitites.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	public Optional<User> findByEmail(String email);

	public boolean deleteByEmail(String email);
}
