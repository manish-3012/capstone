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
	private Long user_id;
    private String email;
    private String password;
    private UserType user_type;

    private String user_name; 
    private String name; 

}
