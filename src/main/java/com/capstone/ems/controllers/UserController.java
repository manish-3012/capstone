package com.capstone.ems.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.ems.domain.dto.UserDto;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.enums.UserType;
import com.capstone.ems.mapper.Mapper;
import com.capstone.ems.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;

    public UserController(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/employees/register")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
    	UserEntity userEntity = userMapper.mapFrom(userDto);
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserEntity newUser = userService.createUser(userEntity);
        return ResponseEntity.ok(userMapper.mapTo(newUser));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(userMapper::mapTo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }
    
    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal) {
    	return principal.getName();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserEntity user = userService.getUserById(userId);
        return ResponseEntity.ok(userMapper.mapTo(user));
    }

    public UserType getUserType(Long userId) {
        UserEntity user = userService.getUserById(userId);
        return user.getUserType();
    }
    
    @PutMapping("/{userId}/type/{userType}")
    public ResponseEntity<UserDto> updateUserType(@PathVariable Long userId, @PathVariable UserType userType) {
        userService.updateUserType(userId, userType);
        UserEntity user = userService.getUserById(userId);
        return ResponseEntity.ok(userMapper.mapTo(user));
    }
}