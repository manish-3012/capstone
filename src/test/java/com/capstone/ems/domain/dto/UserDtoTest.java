package com.capstone.ems.domain.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.capstone.ems.enums.UserType;

public class UserDtoTest {

    @Test
    public void testGettersAndSetters() {
        Long userId = 1L;
        String email = "john.doe@example.com";
        String password = "password123";
        UserType role = UserType.EMPLOYEE;
        Long employeeId = 2L;
        String username = "johndoe";
        String name = "John Doe";

        UserDto userDto = new UserDto();

        userDto.setUserId(userId);
        userDto.setEmail(email);
        userDto.setPassword(password);
        userDto.setRole(role);
        userDto.setEmployeeId(employeeId);
        userDto.setUsername(username);
        userDto.setName(name);

        assertEquals(userId, userDto.getUserId());
        assertEquals(email, userDto.getEmail());
        assertEquals(password, userDto.getPassword());
        assertEquals(role, userDto.getRole());
        assertEquals(employeeId, userDto.getEmployeeId());
        assertEquals(username, userDto.getUsername());
        assertEquals(name, userDto.getName());
    }

    @Test
    public void testBuilder() {
        Long userId = 1L;
        String email = "john.doe@example.com";
        String password = "password123";
        UserType role = UserType.EMPLOYEE;
        Long employeeId = 2L;
        String username = "johndoe";
        String name = "John Doe";

        UserDto userDto = UserDto.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .role(role)
                .employeeId(employeeId)
                .username(username)
                .name(name)
                .build();

        assertEquals(userId, userDto.getUserId());
        assertEquals(email, userDto.getEmail());
        assertEquals(password, userDto.getPassword());
        assertEquals(role, userDto.getRole());
        assertEquals(employeeId, userDto.getEmployeeId());
        assertEquals(username, userDto.getUsername());
        assertEquals(name, userDto.getName());
    }

    @Test
    public void testNoArgsConstructor() {
        UserDto userDto = new UserDto();

        assertNull(userDto.getUserId());
        assertNull(userDto.getEmail());
        assertNull(userDto.getPassword());
        assertNull(userDto.getRole());
        assertNull(userDto.getEmployeeId());
        assertNull(userDto.getUsername());
        assertNull(userDto.getName());
    }
}