package com.capstone.ems.service;

import java.util.List;

import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.enums.UserType;

public interface UserService {
    UserEntity createUser(UserEntity user);
    List<UserEntity> getAllUsers();
    UserEntity getUserById(Long userId);
    void updateRole(Long userId, UserType userType);
    UserEntity getUserByMail(String email);
    UserEntity getUserByUsername(String username);
    void delete(Long id);
}
