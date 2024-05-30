package com.capstone.ems.domain.dto;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ProjectDtoTests {

    @Test
    public void testGettersAndSetters() {
        Long id = 1L;
        String name = "Project X";
        String description = "This is a test project";
        List<String> skills = Arrays.asList("Java", "Python");
        List<Long> employeeIds = Arrays.asList(2L, 3L);
        Long manager = 4L;

        ProjectDto projectDto = new ProjectDto();

        projectDto.setId(id);
        projectDto.setName(name);
        projectDto.setDescription(description);
        projectDto.setSkills(skills);
        projectDto.setEmployeeIds(employeeIds);
        projectDto.setManager(manager);

        assertEquals(id, projectDto.getId());
        assertEquals(name, projectDto.getName());
        assertEquals(description, projectDto.getDescription());
        assertEquals(skills, projectDto.getSkills());
        assertEquals(employeeIds, projectDto.getEmployeeIds());
        assertEquals(manager, projectDto.getManager());
    }

    @Test
    public void testBuilder() {
        Long id = 1L;
        String name = "Project X";
        String description = "This is a test project";
        List<String> skills = Arrays.asList("Java", "Python");
        List<Long> employeeIds = Arrays.asList(2L, 3L);
        Long manager = 4L;

        ProjectDto projectDto = ProjectDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .skills(skills)
                .employeeIds(employeeIds)
                .manager(manager)
                .build();

        assertEquals(id, projectDto.getId());
        assertEquals(name, projectDto.getName());
        assertEquals(description, projectDto.getDescription());
        assertEquals(skills, projectDto.getSkills());
        assertEquals(employeeIds, projectDto.getEmployeeIds());
        assertEquals(manager, projectDto.getManager());
    }

    @Test
    public void testNoArgsConstructor() {
        ProjectDto projectDto = new ProjectDto();

        assertNull(projectDto.getId());
        assertNull(projectDto.getName());
        assertNull(projectDto.getDescription());
        assertNull(projectDto.getSkills());
        assertNull(projectDto.getEmployeeIds());
        assertNull(projectDto.getManager());
    }
}