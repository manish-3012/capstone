package com.capstone.ems.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.capstone.ems.domain.entities.EmployeeEntity.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long empId;
    private Long userId;
    private String name;
    private List<String> skills;
    private Long managerId;
    private Long projectId;
    private String email;
    private Role role;
    
    public enum Role {
        ADMIN,
        EMPLOYEE,
        MANAGER
    }
}