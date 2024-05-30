package com.capstone.ems.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.capstone.ems.domain.dto.RequestDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.enums.RequestStatus;
import com.capstone.ems.enums.UserType;
import com.capstone.ems.mapper.Mapper;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.ProjectService;
import com.capstone.ems.service.RequestService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RequestService requestService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ProjectService projectService;

    @Mock
    private Mapper<RequestEntity, RequestDto> requestMapper;

    @InjectMocks
    private RequestController requestController;

    private RequestEntity requestEntity;
    private EmployeeEntity authenticatedEmployee;
    private RequestDto requestDto;
    private List<RequestEntity> requestEntities;
    private List<RequestDto> requestDtos;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
        
        authenticatedEmployee = new EmployeeEntity();
        authenticatedEmployee.setEmpId(1L);

        requestEntity = new RequestEntity();
        requestEntity.setReqId(1L);
        requestEntity.setStatus(RequestStatus.PENDING);

        requestDto = new RequestDto();
        requestDto.setReqId(1L);
        requestDto.setStatus(RequestStatus.PENDING);

        requestEntities = Arrays.asList(requestEntity);
        requestDtos = Arrays.asList(requestDto);
    }

    @Test
    public void testAdminGetAllRequests() throws Exception {
        when(requestService.findAll()).thenReturn(requestEntities);
        when(requestMapper.mapTo(any(RequestEntity.class))).thenReturn(requestDto);

        mockMvc.perform(get("/admin/all-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reqId").value(requestDto.getReqId()))
                .andExpect(jsonPath("$[0].status").value(requestDto.getStatus().toString()));
    }

    @Test
    void testManagerGetAllRequests() {
        RequestEntity request1 = new RequestEntity();
        request1.setManager(authenticatedEmployee);
        RequestEntity request2 = new RequestEntity();
        request2.setManager(authenticatedEmployee);

        List<RequestEntity> requests = Arrays.asList(request1, request2);
        List<RequestDto> requestDtos = Arrays.asList(new RequestDto(), new RequestDto());

        when(employeeService.getAuthenticatedEmployee()).thenReturn(authenticatedEmployee);
        when(requestService.findAll()).thenReturn(requests);
        when(requestMapper.mapTo(request1)).thenReturn(requestDtos.get(0));
        when(requestMapper.mapTo(request2)).thenReturn(requestDtos.get(1));

        ResponseEntity<List<RequestDto>> responseEntity = requestController.managerGetAllRequests();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(requestDtos, responseEntity.getBody());
    }

    @Test
    void testCreateRequest() {
        RequestDto requestDto = new RequestDto();
        requestDto.setProjectId(1L);

        RequestEntity requestEntity = new RequestEntity();
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
        requestEntity.setProject(projectEntity);

        // Initialize the employeeIds list
        List<Long> employeeIds = new ArrayList<>();
        employeeIds.add(1L); // Add a valid employee ID
        requestEntity.setEmployeeIds(employeeIds);

        EmployeeEntity employee = new EmployeeEntity();

        when(employeeService.getAuthenticatedEmployee()).thenReturn(employee);
        when(requestMapper.mapFrom(requestDto)).thenReturn(requestEntity);
        when(projectService.findOne(1L)).thenReturn(Optional.of(projectEntity));
        when(employeeService.findOne(1L)).thenReturn(Optional.of(new EmployeeEntity())); // Return a valid EmployeeEntity for the given ID
        when(requestService.save(any(RequestEntity.class))).thenReturn(requestEntity);
        when(requestMapper.mapTo(requestEntity)).thenReturn(requestDto);

        ResponseEntity<RequestDto> responseEntity = requestController.createRequest(requestDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(requestDto, responseEntity.getBody());
    }
    
    @Test
    public void testAdminGetRequestsByStatus() throws Exception {
        when(requestService.getRequestsByStatus(eq(RequestStatus.PENDING))).thenReturn(requestEntities);
        when(requestMapper.mapTo(any(RequestEntity.class))).thenReturn(requestDto);

        mockMvc.perform(get("/admin/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reqId").value(requestDto.getReqId()))
                .andExpect(jsonPath("$[0].status").value(requestDto.getStatus().toString()));
    }

    @Test
    void testGetRequestsByStatus() {
        String status = "PENDING";
        RequestStatus requestStatus = RequestStatus.PENDING;

        RequestEntity request1 = new RequestEntity();
        request1.setManager(authenticatedEmployee);
        RequestEntity request2 = new RequestEntity();
        request2.setManager(authenticatedEmployee);

        List<RequestEntity> requests = Arrays.asList(request1, request2);
        List<RequestDto> requestDtos = Arrays.asList(new RequestDto(), new RequestDto());

        when(employeeService.getAuthenticatedEmployee()).thenReturn(authenticatedEmployee);
        when(requestService.getRequestsByStatus(requestStatus)).thenReturn(requests);
        when(requestMapper.mapTo(request1)).thenReturn(requestDtos.get(0));
        when(requestMapper.mapTo(request2)).thenReturn(requestDtos.get(1));

        ResponseEntity<List<RequestDto>> responseEntity = requestController.getRequestsByStatus(status);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(requestDtos, responseEntity.getBody());
    }


    @Test
    public void testAdminGetRequestById() throws Exception {
        when(requestService.getRequestById(eq(1L))).thenReturn(Optional.of(requestEntity));
        when(requestMapper.mapTo(any(RequestEntity.class))).thenReturn(requestDto);

        mockMvc.perform(get("/admin/get-request/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reqId").value(requestDto.getReqId()))
                .andExpect(jsonPath("$.status").value(requestDto.getStatus().toString()));
    }

    @Test
    public void testGetRequestById() throws Exception {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setEmpId(1L);
        employee.setUserType(UserType.MANAGER);

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
        requestEntity.setProject(projectEntity);

        EmployeeEntity managerEntity = new EmployeeEntity();
        managerEntity.setEmpId(1L); // Set a valid manager ID
        requestEntity.setManager(managerEntity);

        when(employeeService.getAuthenticatedEmployee()).thenReturn(employee);
        when(requestService.getRequestById(eq(1L))).thenReturn(Optional.of(requestEntity));
        when(requestMapper.mapTo(any(RequestEntity.class))).thenReturn(requestDto);

        mockMvc.perform(get("/manager/get-request/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reqId").value(requestDto.getReqId()))
                .andExpect(jsonPath("$.status").value(requestDto.getStatus().toString()));
    }

    @Test
    void testUpdateRequestStatus() {
        Long requestId = 1L;
        String status = "APPROVED";
        RequestStatus requestStatus = RequestStatus.APPROVED;

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1L); 
        
        RequestEntity updatedRequestEntity = new RequestEntity();
        updatedRequestEntity.setStatus(requestStatus);
        updatedRequestEntity.setProject(projectEntity); 

        EmployeeEntity managerEntity = new EmployeeEntity();
        managerEntity.setEmpId(1L); 
        updatedRequestEntity.setManager(managerEntity);

        List<Long> employeeIds = new ArrayList<>();
        employeeIds.add(1L); 
        updatedRequestEntity.setEmployeeIds(employeeIds);

        when(requestService.updateRequestStatus(requestId, requestStatus)).thenReturn(updatedRequestEntity);

        ResponseEntity<String> responseEntity = requestController.updateRequestStatus(requestId, status);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Request Status Updated to: " + requestStatus.toString(), responseEntity.getBody());
    }

    @Test
    public void testGetRequestsByManager() throws Exception {
        when(requestService.getRequestsByManager(eq(1L))).thenReturn(requestEntities);
        when(requestMapper.mapTo(any(RequestEntity.class))).thenReturn(requestDto);

        mockMvc.perform(get("/admin/managerId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reqId").value(requestDto.getReqId()))
                .andExpect(jsonPath("$[0].status").value(requestDto.getStatus().toString()));
    }

    @Test
    public void testDeleteRequest() throws Exception {
        doNothing().when(requestService).deleteRequest(eq(1L));

        mockMvc.perform(delete("/manager/delete-request/1"))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
