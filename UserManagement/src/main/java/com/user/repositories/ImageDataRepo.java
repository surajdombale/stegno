package com.user.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.user.entitites.ImageData;

@Repository
public interface ImageDataRepo extends JpaRepository<ImageData, Integer> {
	List<ImageData> findByUsernameAndTimeAfter(String username, LocalDateTime date);

	List<ImageData> findByBanIsTrue();
}
