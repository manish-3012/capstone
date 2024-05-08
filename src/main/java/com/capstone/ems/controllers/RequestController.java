package com.capstone.ems.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.ems.domain.dto.RequestDto;
import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.enums.RequestStatus;
import com.capstone.ems.mapper.Mapper;
import com.capstone.ems.service.RequestService;

@RestController
@RequestMapping("/requests")
public class RequestController {
    private final RequestService requestService;
    private final Mapper<RequestEntity, RequestDto> requestMapper;

    public RequestController(RequestService requestService, Mapper<RequestEntity, RequestDto> requestMapper) {
        this.requestService = requestService;
        this.requestMapper = requestMapper;
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@RequestBody RequestDto requestDto) {
    	requestDto.setStatus(RequestStatus.PENDING);
        RequestEntity requestEntity = requestMapper.mapFrom(requestDto);
        RequestEntity savedRequestEntity = requestService.save(requestEntity);
        return new ResponseEntity<>(requestMapper.mapTo(savedRequestEntity), HttpStatus.CREATED);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RequestDto>> getRequestsByStatus(@PathVariable RequestStatus status) {
        List<RequestEntity> requests = requestService.getRequestsByStatus(status);
        List<RequestDto> requestDtos = requests.stream()
                .map(requestMapper::mapTo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDtos);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<RequestDto> getRequestById(@PathVariable Long requestId) {
        Optional<RequestEntity> request = requestService.getRequestById(requestId);
        return request.map(requestEntity -> new ResponseEntity<>(requestMapper.mapTo(requestEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("request/{requestId}/status/{status}")
    public ResponseEntity<String> updateRequestStatus(@PathVariable Long requestId, @PathVariable RequestStatus status) {
    	RequestEntity updatedRequestEntity = requestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok("Request Status Updated to: " + updatedRequestEntity.getStatus().toString());
    }
    
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<RequestDto>> getRequestsByManager(@PathVariable Long managerId) {
        List<RequestEntity> requests = requestService.getRequestsByManager(managerId);
        List<RequestDto> requestDtos = requests.stream()
                .map(requestMapper::mapTo)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDtos);
    }
    
}