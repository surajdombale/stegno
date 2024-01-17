package com.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.entitites.UserEntry;

@Repository
public interface UserEntryRepo extends JpaRepository<UserEntry,Integer> {

}
