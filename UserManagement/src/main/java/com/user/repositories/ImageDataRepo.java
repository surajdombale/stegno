package com.user.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.entitites.ImageData;
@Repository
public interface ImageDataRepo extends JpaRepository<ImageData,Integer> {

}
