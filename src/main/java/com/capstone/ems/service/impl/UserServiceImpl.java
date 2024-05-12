package com.capstone.ems.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.enums.UserType;
import com.capstone.ems.repository.EmployeeRepository;
import com.capstone.ems.repository.UserRepository;
import com.capstone.ems.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {	
	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public UserServiceImpl(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        String username = generateUsername(user.getName());
        user.setUserName(username);
        String email = username + "@nucleuesteq.com";
        user.setEmail(email);
        
        EmployeeEntity emp = EmployeeEntity.builder()
        					.name(user.getName())
        					.email(email)
        					.userType(user.getRole())
        					.userName(username)
        					.build();

        employeeRepository.save(emp);
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    @Override
    public void updateRole(Long userId, UserType userType) {
        UserEntity user = getUserById(userId);
        user.setRole(userType);
        userRepository.save(user);
    }
    
    @Override
    public UserEntity getUserByMail(String email) {
    	return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    private String generateUsername(String name) {
        String[] nameParts = name.split(" ");
        StringBuilder username = new StringBuilder();

        username.append(nameParts[0].substring(0, Math.min(nameParts[0].length(), 4)));

        if (nameParts.length > 1) {
            username.append(nameParts[1].substring(0, Math.min(nameParts[1].length(), 2)));
        }

        int totalDigits = 0;
        for (String part : nameParts) {
            for (char c : part.toCharArray()) {
                if (Character.isLetter(c)) {
                    totalDigits++;
                }
            }
        }

        username.append(totalDigits);

        return username.toString().toLowerCase();
    }
}