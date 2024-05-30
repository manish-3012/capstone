package com.capstone.ems.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ProjectEntityTests {

    @Test
    void testSetAndGetName() {
        ProjectEntity project = new ProjectEntity();

        project.setName("Project X");

        assertEquals("Project X", project.getName());
    }

    @Test
    void testSetAndGetDescription() {
        ProjectEntity project = new ProjectEntity();

        project.setDescription("This is a test project.");

        assertEquals("This is a test project.", project.getDescription());
    }

    @Test
    void testSetAndGetSkills() {
        ProjectEntity project = new ProjectEntity();
        List<String> skills = new ArrayList<>();
        skills.add("Java");
        skills.add("Python");

        project.setSkills(skills);

        assertEquals(skills, project.getSkills());
    }

    @Test
    void testSetAndGetEmployeeIds() {
        ProjectEntity project = new ProjectEntity();
        List<EmployeeEntity> employees = new ArrayList<>();
        EmployeeEntity employee1 = new EmployeeEntity();
        EmployeeEntity employee2 = new EmployeeEntity();
        employees.add(employee1);
        employees.add(employee2);

        project.setEmployeeIds(employees);

        assertEquals(employees, project.getEmployeeIds());
    }

    @Test
    void testSetAndGetManager() {
        ProjectEntity project = new ProjectEntity();
        EmployeeEntity manager = new EmployeeEntity();

        project.setManager(manager);

        assertEquals(manager, project.getManager());
    }
}