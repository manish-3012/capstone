package com.capstone.ems.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.repository.UserRepository;

class UserEntityDetailServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserEntityDetailService userEntityDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername() {
        String username = "john.doe@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(username);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userEntityDetailService.loadUserByUsername(username);

        assertNotNull(userDetails);
    }

}