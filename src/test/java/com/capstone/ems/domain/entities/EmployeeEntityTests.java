package com.capstone.ems.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.capstone.ems.enums.UserType;

class EmployeeEntityTests {

    @Test
    void testSetAndGetEmpId() {
        EmployeeEntity employee = new EmployeeEntity();

        employee.setEmpId(1L);

        assertEquals(1L, employee.getEmpId());
    }

    @Test
    void testSetAndGetUsername() {
        EmployeeEntity employee = new EmployeeEntity();

        employee.setUsername("johndoe");

        assertEquals("johndoe", employee.getUsername());
    }

    @Test
    void testSetAndGetEmail() {
        EmployeeEntity employee = new EmployeeEntity();

        employee.setEmail("john.doe@example.com");

        assertEquals("john.doe@example.com", employee.getEmail());
    }

    @Test
    void testSetAndGetName() {
        EmployeeEntity employee = new EmployeeEntity();

        employee.setName("John Doe");

        assertEquals("John Doe", employee.getName());
    }

    @Test
    void testSetAndGetSkills() {
        EmployeeEntity employee = new EmployeeEntity();
        List<String> skills = new ArrayList<>();
        skills.add("Java");
        skills.add("Python");

        employee.setSkills(skills);

        assertEquals(skills, employee.getSkills());
    }

    @Test
    void testSetAndGetManager() {
        // Arrange
        EmployeeEntity employee = new EmployeeEntity();
        EmployeeEntity manager = new EmployeeEntity();

        // Act
        employee.setManager(manager);

        // Assert
        assertEquals(manager, employee.getManager());
    }

    @Test
    void testSetAndGetManagedProjectIds() {
        EmployeeEntity employee = new EmployeeEntity();
        List<ProjectEntity> managedProjects = new ArrayList<>();
        ProjectEntity project1 = new ProjectEntity();
        ProjectEntity project2 = new ProjectEntity();
        managedProjects.add(project1);
        managedProjects.add(project2);

        employee.setManagedProjectIds(managedProjects);

        assertEquals(managedProjects, employee.getManagedProjectIds());
    }

    @Test
    void testSetAndGetProject() {
        EmployeeEntity employee = new EmployeeEntity();
        ProjectEntity project = new ProjectEntity();

        employee.setProject(project);

        assertEquals(project, employee.getProject());
    }

    @Test
    void testSetAndGetUserType() {
        EmployeeEntity employee = new EmployeeEntity();

        employee.setUserType(UserType.ADMIN);

        assertEquals(UserType.ADMIN, employee.getUserType());
    }

    @Test
    void testSetAndGetUser() {
        EmployeeEntity employee = new EmployeeEntity();
        UserEntity user = new UserEntity();

        employee.setUser(user);

        assertEquals(user, employee.getUser());
    }
}