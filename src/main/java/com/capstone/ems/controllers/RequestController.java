package com.capstone.ems.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.ems.domain.dto.RequestDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.enums.RequestStatus;
import com.capstone.ems.mapper.Mapper;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.ProjectService;
import com.capstone.ems.service.RequestService;

@RestController
public class RequestController {
    private final RequestService requestService;
    private final Mapper<RequestEntity, RequestDto> requestMapper;
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    public RequestController(RequestService requestService, 
    		Mapper<RequestEntity, RequestDto> requestMapper,
    		EmployeeService employeeService,
    		ProjectService projectService) {
        this.requestService = requestService;
        this.requestMapper = requestMapper;
        this.employeeService = employeeService;
        this.projectService = projectService;
    }
    
    @GetMapping("/admin/all-requests")
    public ResponseEntity<List<RequestDto>> adminGetAllRequests() {
        List<RequestEntity> requests = requestService.findAll();
        List<RequestDto> requestDtos = requests.stream()
                .map(requestMapper::mapTo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDtos);
    }

    @GetMapping("/manager/all-requests")
    public ResponseEntity<List<RequestDto>> managerGetAllRequests() {
    	EmployeeEntity employee = employeeService.getAuthenticatedEmployee();
    	
        List<RequestEntity> requests = requestService.findAll();
        List<RequestEntity> filteredRequests = requests.stream()
                .filter(request -> request.getManager().getEmpId().equals(employee.getEmpId()))
                .collect(Collectors.toList());

        List<RequestDto> requestDtos = filteredRequests.stream()
                .map(requestMapper::mapTo)
                .collect(Collectors.toList());

        return ResponseEntity.ok(requestDtos);
    }

    @PostMapping("/manager/create-request")
    public ResponseEntity<RequestDto> createRequest(@RequestBody RequestDto requestDto) {
    	if (requestDto.getProjectId() == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }
    	EmployeeEntity employee = employeeService.getAuthenticatedEmployee();
    	requestDto.setManagerId(employee.getEmpId());
    	requestDto.setStatus(RequestStatus.PENDING);
    	System.out.println("RequestDto is created till: " + requestDto);
        RequestEntity requestEntity = requestMapper.mapFrom(requestDto);
        
        System.out.println("RequestEntity is created till: " + requestEntity);
        projectService.findOne(requestEntity.getProject().getId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Long> employeeIds = requestEntity.getEmployeeIds();
        for (Long employeeId : employeeIds) {
            employeeService.findOne(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee with ID " + employeeId + " not found"));
        }
        
        RequestEntity savedRequestEntity = requestService.save(requestEntity);
        return new ResponseEntity<>(requestMapper.mapTo(savedRequestEntity), HttpStatus.CREATED);
    }
    
    @GetMapping("admin/status/{status}")
    public ResponseEntity<List<RequestDto>> adminGetRequestsByStatus(@PathVariable String status) {
    	RequestStatus requestStatus = RequestStatus.valueOf(status.toUpperCase()); 
        List<RequestEntity> requests = requestService.getRequestsByStatus(requestStatus);
        List<RequestDto> requestDtos = requests.stream()
                .map(requestMapper::mapTo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDtos);
    }
    
    @GetMapping("/manager/status/{status}")
    public ResponseEntity<List<RequestDto>> getRequestsByStatus(@PathVariable String status) {
    	EmployeeEntity employee = employeeService.getAuthenticatedEmployee();
    	RequestStatus requestStatus = RequestStatus.valueOf(status.toUpperCase()); 
    	
        List<RequestEntity> requests = requestService.getRequestsByStatus(requestStatus);
        List<RequestEntity> filteredRequests = requests.stream()
                .filter(request -> request.getManager() != null && request.getManager().getEmpId().equals(employee.getEmpId()))
                .collect(Collectors.toList());

        List<RequestDto> requestDtos = filteredRequests.stream()
                .map(requestMapper::mapTo)
                .collect(Collectors.toList());

        return ResponseEntity.ok(requestDtos);
    }
    
    @GetMapping("/admin/get-request/{requestId}")
    public ResponseEntity<RequestDto> adminGetRequestById(@PathVariable Long requestId) {
    	Optional<RequestEntity> foundRequest = requestService.getRequestById(requestId);
        return foundRequest.map(requestEntity -> new ResponseEntity<>(requestMapper.mapTo(requestEntity), HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/manager/get-request/{requestId}")
    public ResponseEntity<RequestDto> getRequestById(@PathVariable Long requestId) {
        Optional<RequestEntity> foundRequest = requestService.getRequestById(requestId);
        EmployeeEntity employee = employeeService.getAuthenticatedEmployee();
        RequestEntity request = foundRequest.orElseThrow(() -> new RuntimeException("Request not found"));

        if (employee.getEmpId().equals(request.getManager().getEmpId())) {
            return foundRequest.map(requestEntity -> new ResponseEntity<>(requestMapper.mapTo(requestEntity), HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        }
    }

    @PutMapping("/admin/request/{requestId}/status/{status}")
    public ResponseEntity<String> updateRequestStatus(@PathVariable Long requestId, @PathVariable String status) {
    	RequestStatus requestStatus = RequestStatus.valueOf(status.toUpperCase()); 
    	
    	RequestEntity updatedRequestEntity = requestService.updateRequestStatus(requestId, requestStatus);

    	if (requestStatus == RequestStatus.APPROVED) {
            Long projectId = updatedRequestEntity.getProject().getId();
            Long managerId = updatedRequestEntity.getManager().getEmpId();
            
            List<Long> employeeIds = updatedRequestEntity.getEmployeeIds();
            for (Long employeeId : employeeIds) {
            	employeeService.assignManagerToEmployee(employeeId, managerId);
            	employeeService.assignProjectToEmployee(employeeId, projectId);
            }
            
            employeeService.assignProjectToEmployee(managerId, projectId);
        }
    	
        return ResponseEntity.ok("Request Status Updated to: " + updatedRequestEntity.getStatus().toString());
    }
    
    @GetMapping("admin/managerId/{managerId}")
    public ResponseEntity<List<RequestDto>> getRequestsByManager(@PathVariable Long managerId) {
        List<RequestEntity> requests = requestService.getRequestsByManager(managerId);
        List<RequestDto> requestDtos = requests.stream()
                .map(requestMapper::mapTo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDtos);
    }
    
    @DeleteMapping("manager/delete-request/{requestId}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long requestId) {
        requestService.deleteRequest(requestId);
        return ResponseEntity.noContent().build();
    }
    
}