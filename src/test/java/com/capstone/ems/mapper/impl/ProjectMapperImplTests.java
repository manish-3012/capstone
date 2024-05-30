package com.capstone.ems.mapper.impl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.capstone.ems.domain.dto.ProjectDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;

class ProjectMapperImplTests {

    private ProjectMapperImpl projectMapper;

    @BeforeEach
    void setUp() {
        projectMapper = new ProjectMapperImpl();
    }

    @Test
    void testMapToWithNullInput() {
        assertNull(projectMapper.mapTo(null));
    }

    @Test
    void testMapToWithCompleteData() {
        ProjectEntity projectEntity = createProjectEntity();

        ProjectDto projectDto = projectMapper.mapTo(projectEntity);

        assertNotNull(projectDto);
        assertEquals(1L, projectDto.getId());
        assertEquals("Project A", projectDto.getName());
        assertEquals("This is Project A", projectDto.getDescription());
        assertEquals(Arrays.asList("Java", "Python"), projectDto.getSkills());
        assertEquals(Arrays.asList(2L, 3L), projectDto.getEmployeeIds());
        assertEquals(4L, projectDto.getManager());
    }

    @Test
    void testMapFromWithNullInput() {
        assertNull(projectMapper.mapFrom(null));
    }

    @Test
    void testMapFromWithCompleteData() {
        ProjectDto projectDto = createProjectDto();

        ProjectEntity projectEntity = projectMapper.mapFrom(projectDto);

        assertNotNull(projectEntity);
        assertEquals(1L, projectEntity.getId());
        assertEquals("Project A", projectEntity.getName());
        assertEquals("This is Project A", projectEntity.getDescription());
        assertEquals(Arrays.asList("Java", "Python"), projectEntity.getSkills());
        assertNotNull(projectEntity.getEmployeeIds());
        assertEquals(2, projectEntity.getEmployeeIds().size());
        assertTrue(projectEntity.getEmployeeIds().stream().anyMatch(e -> e.getEmpId().equals(2L)));
        assertTrue(projectEntity.getEmployeeIds().stream().anyMatch(e -> e.getEmpId().equals(3L)));
        assertNotNull(projectEntity.getManager());
        assertEquals(4L, projectEntity.getManager().getEmpId());
    }

    private ProjectEntity createProjectEntity() {
        EmployeeEntity employee1 = new EmployeeEntity();
        employee1.setEmpId(2L);

        EmployeeEntity employee2 = new EmployeeEntity();
        employee2.setEmpId(3L);

        EmployeeEntity manager = new EmployeeEntity();
        manager.setEmpId(4L);

        return ProjectEntity.builder()
                .id(1L)
                .name("Project A")
                .description("This is Project A")
                .skills(Arrays.asList("Java", "Python"))
                .employeeIds(Arrays.asList(employee1, employee2))
                .manager(manager)
                .build();
    }

    private ProjectDto createProjectDto() {
        return ProjectDto.builder()
                .id(1L)
                .name("Project A")
                .description("This is Project A")
                .skills(Arrays.asList("Java", "Python"))
                .employeeIds(Arrays.asList(2L, 3L))
                .manager(4L)
                .build();
    }
}