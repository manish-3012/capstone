package com.capstone.ems.mapper.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.capstone.ems.domain.dto.UserDto;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.enums.UserType;

class UserMapperImplTests {

    private UserMapperImpl userMapper;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        userMapper = new UserMapperImpl(modelMapper);
    }

    @Test
    void testMapToWithCompleteData() {
        UserEntity userEntity = createUserEntity();

        UserDto userDto = userMapper.mapTo(userEntity);

        assertNotNull(userDto);
        assertEquals(1L, userDto.getUserId());
        assertEquals("john.doe@example.com", userDto.getEmail());
        assertEquals("John Doe", userDto.getName());
        assertEquals(UserType.ADMIN, userDto.getRole());
    }

    @Test
    void testMapFromWithCompleteData() {
        UserDto userDto = createUserDto();

        UserEntity userEntity = userMapper.mapFrom(userDto);

        assertNotNull(userEntity);
        assertEquals(1L, userEntity.getUserId());
        assertEquals("john.doe@example.com", userEntity.getEmail());
        assertEquals("John Doe", userEntity.getName());
        assertEquals(UserType.ADMIN, userEntity.getRole());
    }

    private UserEntity createUserEntity() {
        return UserEntity.builder()
                .userId(1L)
                .email("john.doe@example.com")
                .name("John Doe")
                .role(UserType.ADMIN)
                .build();
    }

    private UserDto createUserDto() {
        return UserDto.builder()
                .userId(1L)
                .email("john.doe@example.com")
                .name("John Doe")
                .role(UserType.ADMIN)
                .build();
    }
}