package com.capstone.ems.service;

import com.capstone.ems.domain.dto.ReqRes;
import com.capstone.ems.domain.entities.UserEntity;

public interface UserManagementService {

    ReqRes register(ReqRes registrationRequest);

    ReqRes login(ReqRes loginRequest);

    ReqRes refreshToken(ReqRes refreshTokenRequest);

    ReqRes getAllUsers();

    ReqRes getUsersById(Long id);

    ReqRes deleteUser(Long userId);

    ReqRes updateUser(Long userId, UserEntity updatedUser);

    ReqRes getMyInfo(String email);
    
    public UserEntity getUserByUsername(String username);
}