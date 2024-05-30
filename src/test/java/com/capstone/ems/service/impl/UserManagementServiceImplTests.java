package com.capstone.ems.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.capstone.ems.auth.JwtUtils;
import com.capstone.ems.domain.dto.ReqRes;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.enums.UserType;
import com.capstone.ems.repository.EmployeeRepository;
import com.capstone.ems.repository.UserRepository;

public class UserManagementServiceImplTests {

	@Mock
    private UserRepository userRepository;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtUtils jwtUtils;
    
    @Mock
    private EmployeeRepository employeeRepository;
    
    @InjectMocks
    private UserManagementServiceImpl userManagementService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testRegister() {
        ReqRes registrationRequest = new ReqRes();
        registrationRequest.setName("John Doe");
        registrationRequest.setRole(UserType.ADMIN);
        registrationRequest.setPassword("password");

        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmpId(1L);
        employeeEntity.setUsername("johndoe123");

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);

        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        ReqRes result = userManagementService.register(registrationRequest);

        assertNotNull(result.getOurUsers());
        assertEquals(200, result.getStatusCode());
        assertEquals("User Saved Successfully", result.getMessage());
    }

    @Test
    void testLogin() {
        ReqRes loginRequest = new ReqRes();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("john.doe@example.com");
        userEntity.setRole(UserType.ADMIN);

        String jwtToken = "mockJwtToken";
        String refreshToken = "mockRefreshToken";

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(userEntity));
        when(jwtUtils.generateToken(userEntity)).thenReturn(jwtToken);
        when(jwtUtils.generateRefreshToken(any(), eq(userEntity))).thenReturn(refreshToken);

        ReqRes result = userManagementService.login(loginRequest);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode());
        assertEquals("Successfully Logged In", result.getMessage());
        assertEquals(jwtToken, result.getToken());
        assertEquals(refreshToken, result.getRefreshToken());
        assertEquals(UserType.ADMIN, result.getRole());
        assertEquals("24Hrs", result.getExpirationTime());
    }

    @Test
    public void testRefreshToken_Successful() {
        ReqRes refreshTokenRequest = new ReqRes();
        refreshTokenRequest.setToken("validToken");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("testuser@nucleusteq.com");

        when(jwtUtils.extractUsername("validToken")).thenReturn("testuser@nucleusteq.com");
        when(userRepository.findByEmail("testuser@nucleusteq.com")).thenReturn(Optional.of(userEntity));
        when(jwtUtils.isTokenValid("validToken", userEntity)).thenReturn(true);
        when(jwtUtils.generateToken(userEntity)).thenReturn("newToken");

        ReqRes response = userManagementService.refreshToken(refreshTokenRequest);

        assertEquals(200, response.getStatusCode());
        assertEquals("newToken", response.getToken());
        assertEquals("validToken", response.getRefreshToken());
        assertEquals("24Hr", response.getExpirationTime());
        assertEquals("Successfully Refreshed Token", response.getMessage());
    }

    @Test
    public void testGetAllUsers_Successful() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);

        when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity));

        ReqRes response = userManagementService.getAllUsers();

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getOurUsersList());
        assertEquals(1, response.getOurUsersList().size());
    }

    @Test
    public void testGetUsersById_UserFound_Successful() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        ReqRes response = userManagementService.getUsersById(userId);

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getOurUsers());
        assertEquals(userId, response.getOurUsers().getUserId());
    }

    @Test
    public void testGetUsersById_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ReqRes response = userManagementService.getUsersById(userId);

        assertEquals(500, response.getStatusCode());
        assertNull(response.getOurUsers());
        assertEquals("Error occurred: User Not found", response.getMessage());
    }
    
    @Test
    void testDeleteUser() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(userId);

        ReqRes result = userManagementService.deleteUser(userId);

        assertEquals(200, result.getStatusCode());
        assertEquals("User deleted successfully", result.getMessage());
    }

    @Test
    void testGetUserByUsername() {
        String username = "johndoe";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserEntity result = userManagementService.getUserByUsername(username);

        assertNotNull(result);
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        UserEntity existingUser = new UserEntity();
        existingUser.setUserId(userId);
        existingUser.setName("John Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setRole(UserType.ADMIN);

        UserEntity updatedUser = new UserEntity();
        updatedUser.setName("Jane Smith");
        updatedUser.setEmail("jane.smith@example.com");
        updatedUser.setRole(UserType.EMPLOYEE);
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updatedUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReqRes result = userManagementService.updateUser(userId, updatedUser);

        assertNotNull(result.getOurUsers());
        assertEquals(200, result.getStatusCode());
        assertEquals("User updated successfully", result.getMessage());
        assertEquals("Jane Smith", result.getOurUsers().getName());
        assertEquals("jane.smith@example.com", result.getOurUsers().getEmail());
        assertEquals(UserType.EMPLOYEE, result.getOurUsers().getRole());
        assertNotEquals(updatedUser.getPassword(), result.getOurUsers().getPassword());
    }

    @Test
    void testGetMyInfo() {
        String email = "john.doe@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        ReqRes result = userManagementService.getMyInfo(email);

        assertNotNull(result.getOurUsers());
        assertEquals(200, result.getStatusCode());
        assertEquals("successful", result.getMessage());
        assertEquals(email, result.getOurUsers().getEmail());
    }
}
