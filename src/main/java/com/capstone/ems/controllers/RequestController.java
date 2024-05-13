package com.capstone.ems.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.capstone.ems.service.RequestService;

@RestController
public class RequestController {
    private final RequestService requestService;
    private final Mapper<RequestEntity, RequestDto> requestMapper;
    private final EmployeeService employeeService;

    public RequestController(RequestService requestService, 
    		Mapper<RequestEntity, RequestDto> requestMapper,
    		EmployeeService employeeService) {
        this.requestService = requestService;
        this.requestMapper = requestMapper;
        this.employeeService = employeeService;
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
                .filter(request -> request.getManagerId().equals(employee.getEmpId()))
                .collect(Collectors.toList());

        List<RequestDto> requestDtos = filteredRequests.stream()
                .map(requestMapper::mapTo)
                .collect(Collectors.toList());

        return ResponseEntity.ok(requestDtos);
    }

    @PostMapping("/manager/create-request")
    public ResponseEntity<RequestDto> createRequest(@RequestBody RequestDto requestDto) {
    	requestDto.setStatus(RequestStatus.PENDING);
    
        RequestEntity requestEntity = requestMapper.mapFrom(requestDto);
        RequestEntity savedRequestEntity = requestService.save(requestEntity);
        return new ResponseEntity<>(requestMapper.mapTo(savedRequestEntity), HttpStatus.CREATED);
    }

    @GetMapping("admin/status/{status}")
    public ResponseEntity<List<RequestDto>> adminGetRequestsByStatus(@PathVariable RequestStatus status) {
        List<RequestEntity> requests = requestService.getRequestsByStatus(status);
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
                .filter(request -> request.getManagerId().equals(employee.getEmpId()))
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

        if (employee.getEmpId().equals(request.getManagerId())) {
            return foundRequest.map(requestEntity -> new ResponseEntity<>(requestMapper.mapTo(requestEntity), HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); 
        }
    }

    @PutMapping("/admin/request/{requestId}/status/{status}")
    public ResponseEntity<String> updateRequestStatus(@PathVariable Long requestId, @PathVariable RequestStatus status) {
    	RequestEntity updatedRequestEntity = requestService.updateRequestStatus(requestId, status);
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
    
}