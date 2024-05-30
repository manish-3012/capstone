package com.capstone.ems.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.ems.auth.JwtUtils;
import com.capstone.ems.domain.dto.ReqRes;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.repository.EmployeeRepository;
import com.capstone.ems.repository.UserRepository;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.UserManagementService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserManagementServiceImpl implements UserManagementService{


    @Autowired
    private UserRepository usersRepo;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeRepository employeeRepository;
    
    
    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
//            System.out.println("Entered the try block of register with the registration request: " + registrationRequest);

            String username = generateUsername(registrationRequest.getName());
            String email = username + "@nucleusteq.com";
            
            // Create EmployeeEntity first
            EmployeeEntity emp = EmployeeEntity.builder()
                    .name(registrationRequest.getName())
                    .email(email)
                    .userType(registrationRequest.getRole())
                    .username(username)
                    .build();
            EmployeeEntity empResult = employeeRepository.save(emp);
            
            if (empResult != null && empResult.getEmpId() != null) {
//                System.out.println("Employee Saved Successfully: " + empResult);

                // Create UserEntity linked to the saved EmployeeEntity
                UserEntity ourUser = new UserEntity();
                ourUser.setUsername(empResult.getUsername());
                ourUser.setEmail(empResult.getEmail());
                ourUser.setName(empResult.getName());
                ourUser.setRole(empResult.getUserType());
                ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                ourUser.setEmployee(empResult);

                UserEntity ourUsersResult = usersRepo.save(ourUser);

                if (ourUsersResult.getUserId() > 0) {
//                    System.out.println("User Saved Successfully: " + ourUsersResult);
                    resp.setOurUsers(ourUsersResult);
                    resp.setMessage("User Saved Successfully");
                    resp.setStatusCode(200);

                    // Update EmployeeEntity with the linked UserEntity
                    empResult.setUser(ourUsersResult);
                    employeeRepository.save(empResult);
                }
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

//        System.out.println("Returning response from the UserManagementService as: " + resp);
        return resp;
    }

    public ReqRes login(ReqRes loginRequest){
        ReqRes response = new ReqRes();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        try{
            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
            UserEntity users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }


    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<UserEntity> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqRes getUsersById(Long id) {
        ReqRes reqRes = new ReqRes();
        try {
        	UserEntity usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setOurUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteUser(Long userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<UserEntity> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }
    
    @Override
    public UserEntity getUserByUsername(String username) {
        return usersRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    public ReqRes updateUser(Long userId, UserEntity updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<UserEntity> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
            	UserEntity existingUser = userOptional.get();
            	if (updatedUser.getName() != null) {
                    existingUser.setName(updatedUser.getName());
                }
                if (updatedUser.getEmail() != null) {
                    existingUser.setEmail(updatedUser.getEmail());
                }
                if (updatedUser.getRole() != null) {
                    existingUser.setRole(updatedUser.getRole());
                }
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                UserEntity savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<UserEntity> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
    }
    
    public String generateUsername(String name) {
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
