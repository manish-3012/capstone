package com.capstone.ems.domain.dto;


import com.capstone.ems.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long userId;
    private String email;
    private String password;
    private UserType role;
    private Long employeeId;
    private String username; 
    private String name; 
}
