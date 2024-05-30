package com.capstone.ems.controllers;

import com.capstone.ems.domain.dto.ReqRes;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.service.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserManagementControllerTest {

    @Mock
    private UserManagementService userManagementService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserManagementController userManagementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        ReqRes reqRes = new ReqRes();
        when(userManagementService.register(reqRes)).thenReturn(reqRes);

        ResponseEntity<ReqRes> responseEntity = userManagementController.register(reqRes);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reqRes, responseEntity.getBody());
    }

    @Test
    void testLogin() {
        ReqRes reqRes = new ReqRes();
        when(userManagementService.login(reqRes)).thenReturn(reqRes);

        ResponseEntity<ReqRes> responseEntity = userManagementController.login(reqRes);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reqRes, responseEntity.getBody());
    }

    @Test
    void testRefreshToken() {
        ReqRes reqRes = new ReqRes();
        when(userManagementService.refreshToken(reqRes)).thenReturn(reqRes);

        ResponseEntity<ReqRes> responseEntity = userManagementController.refreshToken(reqRes);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reqRes, responseEntity.getBody());
    }

    @Test
    void testGetAllUsers() {
        ReqRes reqRes = new ReqRes();
        when(userManagementService.getAllUsers()).thenReturn(reqRes);

        ResponseEntity<ReqRes> responseEntity = userManagementController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reqRes, responseEntity.getBody());
    }

    @Test
    void testGetUserById() {
        Long userId = 1L;
        ReqRes reqRes = new ReqRes();
        when(userManagementService.getUsersById(userId)).thenReturn(reqRes);

        ResponseEntity<ReqRes> responseEntity = userManagementController.getUSerByID(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reqRes, responseEntity.getBody());
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        ReqRes reqRes = new ReqRes();
        when(userManagementService.updateUser(userId, userEntity)).thenReturn(reqRes);

        ResponseEntity<ReqRes> responseEntity = userManagementController.updateUser(userId, userEntity);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reqRes, responseEntity.getBody());
    }

    @Test
    void testGetMyProfile() {
        String email = "test@example.com";
        ReqRes reqRes = new ReqRes();
        reqRes.setStatusCode(200);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
        when(userManagementService.getMyInfo(email)).thenReturn(reqRes);

        ResponseEntity<ReqRes> responseEntity = userManagementController.getMyProfile();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reqRes, responseEntity.getBody());
    }
    
    @Test
    void testDeleteUser() {
        Long userId = 1L;
        ReqRes reqRes = new ReqRes();
        when(userManagementService.deleteUser(userId)).thenReturn(reqRes);

        ResponseEntity<ReqRes> responseEntity = userManagementController.deleteUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(reqRes, responseEntity.getBody());
    }
}