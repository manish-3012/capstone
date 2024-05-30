package com.capstone.ems.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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

import com.capstone.ems.domain.dto.EmployeeDto;
import com.capstone.ems.domain.dto.ReqRes;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.mapper.Mapper;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.UserManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private UserManagementService userManagementService;

    @Mock
    private Mapper<EmployeeEntity, EmployeeDto> employeeMapper;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeEntity employeeEntity;
    private EmployeeDto employeeDto;
    private List<EmployeeEntity> employeeEntities;
    private List<EmployeeDto> employeeDtos;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

        employeeEntity = new EmployeeEntity();
        employeeEntity.setEmpId(1L);
        employeeEntity.setName("John Doe");

        employeeDto = new EmployeeDto();
        employeeDto.setEmpId(1L);
        employeeDto.setName("John Doe");

        employeeEntities = Arrays.asList(employeeEntity);
        employeeDtos = Arrays.asList(employeeDto);
    }

    @Test
    public void testCreateEmployee() throws Exception {
        when(employeeMapper.mapFrom(any(EmployeeDto.class))).thenReturn(employeeEntity);
        when(employeeService.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
        when(employeeMapper.mapTo(any(EmployeeEntity.class))).thenReturn(employeeDto);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.empId").value(employeeDto.getEmpId()))
                .andExpect(jsonPath("$.name").value(employeeDto.getName()));
    }

    @Test
    public void testListEmployees() throws Exception {
        when(employeeService.findAll()).thenReturn(employeeEntities);
        when(employeeMapper.mapTo(any(EmployeeEntity.class))).thenReturn(employeeDto);

        mockMvc.perform(get("/all/get-employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].empId").value(employeeDto.getEmpId()))
                .andExpect(jsonPath("$[0].name").value(employeeDto.getName()));
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        when(employeeService.findOne(eq(1L))).thenReturn(Optional.of(employeeEntity));
        when(employeeMapper.mapTo(any(EmployeeEntity.class))).thenReturn(employeeDto);

        mockMvc.perform(get("/all/get-employee/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId").value(employeeDto.getEmpId()))
                .andExpect(jsonPath("$.name").value(employeeDto.getName()));
    }

    @Test
    public void testGetEmployeeById_NotFound() throws Exception {
        when(employeeService.findOne(eq(1L))).thenReturn(Optional.empty());

        mockMvc.perform(get("/all/get-employee/{id}", 1L))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testUpdateEmployee() throws Exception {
        when(employeeService.isExists(eq(1L))).thenReturn(true);
        when(employeeMapper.mapFrom(any(EmployeeDto.class))).thenReturn(employeeEntity);
        when(employeeService.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
        when(employeeMapper.mapTo(any(EmployeeEntity.class))).thenReturn(employeeDto);

        mockMvc.perform(put("/admin/update-employee/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId").value(employeeDto.getEmpId()))
                .andExpect(jsonPath("$.name").value(employeeDto.getName()));
    }

    @Test
    public void testUpdateEmployee_NotFound() throws Exception {
        when(employeeService.isExists(eq(1L))).thenReturn(false);

        mockMvc.perform(put("/admin/update-employee/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employeeDto)))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void testGetEmployeeByUserId() throws Exception {
//        Long userId = 1L;
//
//        ReqRes res = new ReqRes();
//        res.setOurUsers(new UserEntity());
//        UserEntity userEntity = res.getOurUsers();
//
//        EmployeeEntity employee = new EmployeeEntity();
//        employee.setEmpId(1L);
//        employee.setName("John Doe");
//
//        when(userManagementService.getUsersById(userId)).thenReturn(res);
//        when(employeeService.findByUser(userEntity)).thenReturn(Optional.of(employee));
//        when(employeeMapper.mapTo(employee)).thenReturn(new EmployeeDto());
//
//        mockMvc.perform(get("/all/get-employee/user-id/{userId}", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value(employee.getName()));
//    }
//
//    @Test
//    public void testGetEmployeeByUserIdNotFound() throws Exception {
//        Long userId = 1L;
//
//        when(userManagementService.getUsersById(userId)).thenReturn(new ReqRes());
//        when(employeeService.findByUser(any(UserEntity.class))).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/all/get-employee/user-id/{userId}", userId))
//                .andExpect(status().isNotFound());
//    }

    
    @Test
    void testGetEmployeesByProjectId() throws Exception {
        Long projectId = 1L;
        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        employeeEntities.add(new EmployeeEntity());
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(new EmployeeDto());
        when(employeeService.findByProject(projectId)).thenReturn(employeeEntities);
        when(employeeMapper.mapTo(any(EmployeeEntity.class))).thenReturn(new EmployeeDto());

        mockMvc.perform(get("/all/get-employees/project/{id}", projectId))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetEmployeesByManagerId() throws Exception {
        Long id = 1L;
        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        employeeEntities.add(new EmployeeEntity());
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(new EmployeeDto());
        when(employeeService.findByManager(id)).thenReturn(employeeEntities);
        when(employeeMapper.mapTo(any(EmployeeEntity.class))).thenReturn(new EmployeeDto());

        mockMvc.perform(get("/all/get-employees/manager/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testGetEmployeeByEmail() throws Exception {
        String email = "test@example.com";
        EmployeeEntity employeeEntity = new EmployeeEntity();
        EmployeeDto employeeDto = new EmployeeDto();
        when(employeeService.findByEmail(email)).thenReturn(Optional.of(employeeEntity));
        when(employeeMapper.mapTo(employeeEntity)).thenReturn(employeeDto);

        mockMvc.perform(get("/all/get-employee/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testGetEmployeeByUserName() throws Exception {
        String username = "testuser";
        EmployeeEntity employeeEntity = new EmployeeEntity();
        EmployeeDto employeeDto = new EmployeeDto();
        when(employeeService.findByUserName(username)).thenReturn(Optional.of(employeeEntity));
        when(employeeMapper.mapTo(employeeEntity)).thenReturn(employeeDto);

        mockMvc.perform(get("/all/get-employee/username/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testGetEmployeesBySkill() throws Exception {
        String skill = "Java";
        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        employeeEntities.add(new EmployeeEntity());
        when(employeeService.findAllBySkillsContaining(skill)).thenReturn(employeeEntities);

        mockMvc.perform(get("/adminmanager/get-employees/skills/{skill}", skill))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testAddSkills() throws Exception {
        Long employeeId = 1L;
        List<String> skills = Arrays.asList("Java", "Spring", "SQL");

        EmployeeEntity employee = new EmployeeEntity();
        employee.setEmpId(employeeId);
        employee.setSkills(new ArrayList<>());

        EmployeeEntity updatedEmployee = new EmployeeEntity();
        updatedEmployee.setEmpId(employeeId);
        updatedEmployee.setSkills(skills);

        when(employeeService.findOne(employeeId)).thenReturn(Optional.of(employee));
        when(employeeService.save(any(EmployeeEntity.class))).thenReturn(updatedEmployee);
        when(employeeMapper.mapTo(updatedEmployee)).thenReturn(new EmployeeDto());

        mockMvc.perform(patch("/manageruser/add-skills/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(skills)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testAddSkillsNotFound() throws Exception {
        Long employeeId = 1L;
        List<String> skills = Arrays.asList("Java", "Spring", "SQL");

        when(employeeService.findOne(employeeId)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/manageruser/add-skills/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(skills)))
                .andExpect(status().isNotFound());
    }
    
    


    @Test
    void testAssignProject() throws Exception {
        Long employeeId = 1L;
        Long projectId = 2L;
        doNothing().when(employeeService).assignProjectToEmployee(employeeId, projectId);

        mockMvc.perform(put("/admin/assign-project/{employeeId}/{projectId}", employeeId, projectId))
                .andExpect(status().isOk());
    }

    @Test
    void testUnassignProject() throws Exception {
        Long employeeId = 1L;
        Long projectId = 2L;
        doNothing().when(employeeService).unassignProjectFromEmployee(employeeId);

        mockMvc.perform(put("/admin/unassign-project/{employeeId}/{projectId}", employeeId, projectId))
                .andExpect(status().isOk());
    }

//    @Test
//    void testDeleteEmployee() throws Exception {
//    	ReqRes res = new ReqRes();
//    	res.setStatusCode(200);
//        Long id = 1L;
//        doNothing().when(employeeService).delete(id);
//        when(userManagementService.deleteUser(id)).thenReturn(res);
//      
//        assertEquals(200, res.getStatusCode());
//        mockMvc.perform(delete("/admin/delete-employee/{id}", id))
//                .andExpect(status().isOk());
//    }

    @Test
    void testPartialUpdateEmployee() throws Exception {
        Long empId = 1L;
        EmployeeDto employeeDto = new EmployeeDto();
        EmployeeEntity employeeEntity = new EmployeeEntity();
        when(employeeService.isExists(empId)).thenReturn(true);
        when(employeeMapper.mapFrom(any(EmployeeDto.class))).thenReturn(employeeEntity);
        when(employeeService.partialUpdate(empId, employeeEntity)).thenReturn(employeeEntity);
        when(employeeMapper.mapTo(any(EmployeeEntity.class))).thenReturn(employeeDto);

        mockMvc.perform(patch("/admin/partial-update-employee/{empId}", empId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }
    
    @Test
    public void testPartialUpdateEmployeeNotFound() throws Exception {
        Long empId = 1L;
        EmployeeDto employeeDto = new EmployeeDto();

        when(employeeService.isExists(eq(empId))).thenReturn(false);

        mockMvc.perform(patch("/admin/partial-update-employee/{empId}", empId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employeeDto)))
                .andExpect(status().isNotFound());
    }

    
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
