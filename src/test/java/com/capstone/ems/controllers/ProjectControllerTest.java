package com.capstone.ems.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.capstone.ems.domain.dto.ProjectDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.enums.UserType;
import com.capstone.ems.mapper.Mapper;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectService projectService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private Mapper<ProjectEntity, ProjectDto> projectMapper;

    @InjectMocks
    private ProjectController projectController;

    private ProjectEntity projectEntity;
    private ProjectDto projectDto;
    private List<ProjectEntity> projectEntities;
    private List<ProjectDto> projectDtos;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();

        projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
        projectEntity.setName("Test Project");

        projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setName("Test Project");

        projectEntities = Arrays.asList(projectEntity);
        projectDtos = Arrays.asList(projectDto);
    }

    @Test
    public void testCreateProject() throws Exception {
        when(projectMapper.mapFrom(any(ProjectDto.class))).thenReturn(projectEntity);
        when(projectService.save(any(ProjectEntity.class))).thenReturn(projectEntity);
        when(projectMapper.mapTo(any(ProjectEntity.class))).thenReturn(projectDto);

        mockMvc.perform(post("/adminmanager/create-project")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(projectDto.getId()))
                .andExpect(jsonPath("$.name").value(projectDto.getName()));
    }

    @Test
    public void testListProjects() throws Exception {
        when(projectService.findAll()).thenReturn(projectEntities);
        when(projectMapper.mapTo(any(ProjectEntity.class))).thenReturn(projectDto);

        mockMvc.perform(get("/admin/get-projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(projectDto.getId()))
                .andExpect(jsonPath("$[0].name").value(projectDto.getName()));
    }

    @Test
    public void testAdminGetProjectById() throws Exception {
        when(projectService.findOne(eq(1L))).thenReturn(Optional.of(projectEntity));
        when(projectMapper.mapTo(any(ProjectEntity.class))).thenReturn(projectDto);

        mockMvc.perform(get("/admin/get-project/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectDto.getId()))
                .andExpect(jsonPath("$.name").value(projectDto.getName()));
    }
    
    @Test
    public void testGetProjectById() throws Exception {
        when(projectService.findOne(eq(1L))).thenReturn(Optional.of(projectEntity));
        when(projectMapper.mapTo(any(ProjectEntity.class))).thenReturn(projectDto);

        mockMvc.perform(get("/manageruser/get-project/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectDto.getId()))
                .andExpect(jsonPath("$.name").value(projectDto.getName()));
    }

    @Test
    public void testUpdateProject() throws Exception {
        when(projectService.isExists(eq(1L))).thenReturn(true);
        when(projectMapper.mapFrom(any(ProjectDto.class))).thenReturn(projectEntity);
        when(projectService.partialUpdate(eq(1L), any(ProjectEntity.class))).thenReturn(projectEntity);
        when(projectMapper.mapTo(any(ProjectEntity.class))).thenReturn(projectDto);

        mockMvc.perform(put("/admin/update-project/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectDto.getId()))
                .andExpect(jsonPath("$.name").value(projectDto.getName()));
    }

    @Test
    public void testUpdateProjectNotFound() throws Exception {
        when(projectService.isExists(eq(1L))).thenReturn(false);

        mockMvc.perform(put("/admin/update-project/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDto)))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testPartialUpdateProject() throws Exception {
        when(projectService.isExists(eq(1L))).thenReturn(true);
        when(projectMapper.mapFrom(any(ProjectDto.class))).thenReturn(projectEntity);
        when(projectService.partialUpdate(eq(1L), any(ProjectEntity.class))).thenReturn(projectEntity);
        when(projectMapper.mapTo(any(ProjectEntity.class))).thenReturn(projectDto);

        mockMvc.perform(patch("/admin/partial-update-project/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectDto.getId()))
                .andExpect(jsonPath("$.name").value(projectDto.getName()));
    }
    
    @Test
    public void testPartialUpdateProjectNotFound() throws Exception {
        when(projectService.isExists(eq(1L))).thenReturn(false);

        mockMvc.perform(patch("/admin/partial-update-project/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(projectDto)))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testDeleteProject() throws Exception {
        doNothing().when(projectService).delete(eq(1L));

        mockMvc.perform(delete("/admin/delete-project/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testAdminGetProjectsByManagerId() throws Exception {
        EmployeeEntity manager = new EmployeeEntity();
        manager.setEmpId(1L);
        manager.setUserType(UserType.MANAGER);
        manager.setManagedProjectIds(projectEntities);

        when(employeeService.findOne(eq(1L))).thenReturn(Optional.of(manager));
        when(projectMapper.mapTo(any(ProjectEntity.class))).thenReturn(projectDto);

        mockMvc.perform(get("/adminmanager/get-project/manager/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(projectDto.getId()))
                .andExpect(jsonPath("$[0].name").value(projectDto.getName()));
    }
    
    @Test
    public void testAdminGetProjectsByManagerIdNotFound() throws Exception {
        when(employeeService.findOne(eq(1L))).thenReturn(Optional.empty());

        mockMvc.perform(get("/adminmanager/get-project/manager/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAdminGetProjectsByManagerIdBadRequest() throws Exception {
        when(employeeService.findOne(eq(1L))).thenReturn(Optional.of(new EmployeeEntity()));

        mockMvc.perform(get("/adminmanager/get-project/manager/1"))
                .andExpect(status().isBadRequest());
    }

    
    @Test
    public void testGetProjectsForManager_ValidManager() throws Exception {
        ProjectDto projectDto1 = new ProjectDto();
        projectDto1.setId(1L);
        projectDto1.setName("Project 1");

        ProjectDto projectDto2 = new ProjectDto();
        projectDto2.setId(2L);
        projectDto2.setName("Project 2");

        EmployeeEntity manager = new EmployeeEntity();
        manager.setUserType(UserType.MANAGER);

        List<ProjectEntity> managedProjects = new ArrayList<>();
        ProjectEntity project1 = new ProjectEntity();
        ProjectEntity project2 = new ProjectEntity();
        managedProjects.add(project1);
        managedProjects.add(project2);
        manager.setManagedProjectIds(managedProjects);

        when(employeeService.getAuthenticatedEmployee()).thenReturn(manager);
        when(employeeService.findOne(manager.getEmpId())).thenReturn(Optional.of(manager));
        when(projectMapper.mapTo(project1)).thenReturn(projectDto1);
        when(projectMapper.mapTo(project2)).thenReturn(projectDto2);

        mockMvc.perform(get("/manager/get-projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(projectDto2.getId()))
                .andExpect(jsonPath("$[0].name").value(projectDto2.getName()))
                .andExpect(jsonPath("$[1].id").value(projectDto2.getId()))
                .andExpect(jsonPath("$[1].name").value(projectDto2.getName()));
    }
    
    @Test
    public void testGetProjectsForManager_EmployeeNotFound() throws Exception {
        EmployeeEntity emp = new EmployeeEntity();
        emp.setEmpId(1L);

        when(employeeService.getAuthenticatedEmployee()).thenReturn(emp);
        when(employeeService.findOne(emp.getEmpId())).thenReturn(Optional.empty());

        mockMvc.perform(get("/manager/get-projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProjectsForManager_EmployeeIsNotManager() throws Exception {
        EmployeeEntity emp = new EmployeeEntity();
        emp.setEmpId(1L);
        emp.setUserType(UserType.EMPLOYEE);

        when(employeeService.getAuthenticatedEmployee()).thenReturn(emp);
        when(employeeService.findOne(emp.getEmpId())).thenReturn(Optional.of(emp));

        mockMvc.perform(get("/manager/get-projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
