package com.capstone.ems.domain.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.enums.UserType;

public class ReqResTest {

    @Test
    public void testGettersAndSetters() {
        int statusCode = 200;
        String error = "No error";
        String message = "Success";
        String token = "abcd1234";
        String refreshToken = "efgh5678";
        String expirationTime = "2023-06-30T12:00:00Z";
        String name = "John Doe";
        UserType role = UserType.ADMIN;
        String email = "john.doe@example.com";
        String password = "password123";
        UserEntity userEntity = new UserEntity();
        List<UserEntity> userList = new ArrayList<>();
        userList.add(userEntity);

        ReqRes reqRes = new ReqRes();

        reqRes.setStatusCode(statusCode);
        reqRes.setError(error);
        reqRes.setMessage(message);
        reqRes.setToken(token);
        reqRes.setRefreshToken(refreshToken);
        reqRes.setExpirationTime(expirationTime);
        reqRes.setName(name);
        reqRes.setRole(role);
        reqRes.setEmail(email);
        reqRes.setPassword(password);
        reqRes.setOurUsers(userEntity);
        reqRes.setOurUsersList(userList);

        assertEquals(statusCode, reqRes.getStatusCode());
        assertEquals(error, reqRes.getError());
        assertEquals(message, reqRes.getMessage());
        assertEquals(token, reqRes.getToken());
        assertEquals(refreshToken, reqRes.getRefreshToken());
        assertEquals(expirationTime, reqRes.getExpirationTime());
        assertEquals(name, reqRes.getName());
        assertEquals(role, reqRes.getRole());
        assertEquals(email, reqRes.getEmail());
        assertEquals(password, reqRes.getPassword());
        assertEquals(userEntity, reqRes.getOurUsers());
        assertEquals(userList, reqRes.getOurUsersList());
    }
}