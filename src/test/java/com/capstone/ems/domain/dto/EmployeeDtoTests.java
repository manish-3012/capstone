package com.capstone.ems.domain.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.capstone.ems.enums.UserType;

public class EmployeeDtoTests {

    @Test
    public void testGettersAndSetters() {
        Long empId = 1L;
        String username = "johndoe";
        String email = "john.doe@example.com";
        String name = "John Doe";
        List<String> skills = Arrays.asList("Java", "Python");
        Long managerId = 2L;
        List<Long> managedProjectIds = Arrays.asList(3L, 4L);
        Long projectId = 5L;
        UserType userType = UserType.EMPLOYEE;
        Long userId = 6L;

        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setEmpId(empId);
        employeeDto.setUsername(username);
        employeeDto.setEmail(email);
        employeeDto.setName(name);
        employeeDto.setSkills(skills);
        employeeDto.setManagerId(managerId);
        employeeDto.setManagedProjectIds(managedProjectIds);
        employeeDto.setProjectId(projectId);
        employeeDto.setUserType(userType);
        employeeDto.setUserId(userId);

        assertEquals(empId, employeeDto.getEmpId());
        assertEquals(username, employeeDto.getUsername());
        assertEquals(email, employeeDto.getEmail());
        assertEquals(name, employeeDto.getName());
        assertEquals(skills, employeeDto.getSkills());
        assertEquals(managerId, employeeDto.getManagerId());
        assertEquals(managedProjectIds, employeeDto.getManagedProjectIds());
        assertEquals(projectId, employeeDto.getProjectId());
        assertEquals(userType, employeeDto.getUserType());
        assertEquals(userId, employeeDto.getUserId());
    }

    @Test
    public void testBuilder() {
        Long empId = 1L;
        String username = "johndoe";
        String email = "john.doe@example.com";
        String name = "John Doe";
        List<String> skills = Arrays.asList("Java", "Python");
        Long managerId = 2L;
        List<Long> managedProjectIds = Arrays.asList(3L, 4L);
        Long projectId = 5L;
        UserType userType = UserType.EMPLOYEE;
        Long userId = 6L;

        EmployeeDto employeeDto = EmployeeDto.builder()
                .empId(empId)
                .username(username)
                .email(email)
                .name(name)
                .skills(skills)
                .managerId(managerId)
                .managedProjectIds(managedProjectIds)
                .projectId(projectId)
                .userType(userType)
                .userId(userId)
                .build();

        assertEquals(empId, employeeDto.getEmpId());
        assertEquals(username, employeeDto.getUsername());
        assertEquals(email, employeeDto.getEmail());
        assertEquals(name, employeeDto.getName());
        assertEquals(skills, employeeDto.getSkills());
        assertEquals(managerId, employeeDto.getManagerId());
        assertEquals(managedProjectIds, employeeDto.getManagedProjectIds());
        assertEquals(projectId, employeeDto.getProjectId());
        assertEquals(userType, employeeDto.getUserType());
        assertEquals(userId, employeeDto.getUserId());
    }
}