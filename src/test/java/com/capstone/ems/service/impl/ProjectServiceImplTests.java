package com.capstone.ems.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.repository.EmployeeRepository;
import com.capstone.ems.repository.ProjectRepository;

class ProjectServiceImplTests {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        EmployeeEntity manager = new EmployeeEntity();
        manager.setEmpId(1L);

        ProjectEntity projectEntity = ProjectEntity.builder()
                .manager(manager)
                .build();

        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        ProjectEntity savedProject = projectService.save(projectEntity);

        assertEquals(projectEntity, savedProject);
        verify(projectRepository, times(1)).save(any(ProjectEntity.class));
    }

    @Test
    void testSave_ManagerIsNull() {
        ProjectEntity projectEntity = ProjectEntity.builder()
                .manager(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> projectService.save(projectEntity));
        verify(projectRepository, times(0)).save(any(ProjectEntity.class));
    }

    @Test
    void testSave_ManagerIdIsNull() {
        EmployeeEntity manager = new EmployeeEntity();
        manager.setEmpId(null);

        ProjectEntity projectEntity = ProjectEntity.builder()
                .manager(manager)
                .build();

        assertThrows(IllegalArgumentException.class, () -> projectService.save(projectEntity));
        verify(projectRepository, times(0)).save(any(ProjectEntity.class));
    }

    @Test
    void testFindAll() {
        List<ProjectEntity> projectEntities = new ArrayList<>();
        projectEntities.add(new ProjectEntity());
        projectEntities.add(new ProjectEntity());

        when(projectRepository.findAll()).thenReturn(projectEntities);

        List<ProjectEntity> foundProjects = projectService.findAll();

        assertEquals(2, foundProjects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void testFindOne() {
        Long projectId = 1L;
        ProjectEntity projectEntity = new ProjectEntity();
        when(projectRepository.findById(eq(projectId))).thenReturn(Optional.of(projectEntity));

        Optional<ProjectEntity> foundProject = projectService.findOne(projectId);

        assertEquals(Optional.of(projectEntity), foundProject);
        verify(projectRepository, times(1)).findById(eq(projectId));
    }

    @Test
    void testIsExists() {
        Long projectId = 1L;
        when(projectRepository.existsById(eq(projectId))).thenReturn(true);

        boolean exists = projectService.isExists(projectId);

        assertEquals(true, exists);
        verify(projectRepository, times(1)).existsById(eq(projectId));
    }

    @Test
    void testPartialUpdate() {
        Long projectId = 1L;
        ProjectEntity updatedProject = new ProjectEntity();
        updatedProject.setName("Updated Project");

        when(projectRepository.findById(eq(projectId))).thenReturn(Optional.of(new ProjectEntity()));
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(updatedProject);

        ProjectEntity savedProject = projectService.partialUpdate(projectId, updatedProject);

        assertEquals("Updated Project", savedProject.getName());
        verify(projectRepository, times(1)).findById(eq(projectId));
        verify(projectRepository, times(1)).save(any(ProjectEntity.class));
    }

    @Test
    void testDelete() {
        Long projectId = 1L;
        doNothing().when(projectRepository).deleteById(eq(projectId));

        projectService.delete(projectId);

        verify(projectRepository, times(1)).deleteById(eq(projectId));
    }
}