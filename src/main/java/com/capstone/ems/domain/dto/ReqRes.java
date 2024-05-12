package com.capstone.ems.domain.dto;

import java.util.List;

import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private UserType role;
    private String email;
    private String password;
    private UserEntity ourUsers;
    private List<UserEntity> ourUsersList;

}