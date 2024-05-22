package com.capstone.ems.domain.dto;

import java.util.List;

import com.capstone.ems.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long empId;
    private String username;
    private String email;
    private String name;
    private List<String> skills;
    private Long managerId;
    private List<Long> managedProjectIds;
    private Long projectId;
    private UserType userType;
    private Long userId;
}
