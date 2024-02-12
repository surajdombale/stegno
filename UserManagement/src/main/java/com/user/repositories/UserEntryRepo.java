package com.user.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.entitites.UserEntry;

@Repository
public interface UserEntryRepo extends JpaRepository<UserEntry, Integer> {
	List<UserEntry> findByUsernameAndTimeAfter(String username, LocalDateTime date);
	List<UserEntry> findByUsername(String username);
	List<UserEntry> findByImageId(Integer imageId);
}
