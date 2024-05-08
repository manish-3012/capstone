package com.capstone.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.enums.UserType;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	List<UserEntity> findByUserType(UserType userType);
	Optional<UserEntity> findByEmail(String email);
	Optional<UserEntity> findByUserName(String username);
}
