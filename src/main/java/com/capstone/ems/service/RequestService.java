package com.capstone.ems.service;

import java.util.List;
import java.util.Optional;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.enums.RequestStatus;

public interface RequestService {
    RequestEntity save(RequestEntity request);
    List<RequestEntity> findAll();
    
    Optional<RequestEntity> getRequestById(Long requestId);
    
    List<RequestEntity> getRequestsByManager(Long manager);
    
    List<RequestEntity> getRequestsByStatus(RequestStatus status);
    
    RequestEntity updateRequestStatus(Long requestId, RequestStatus status);
    
    void deleteRequest(Long requestId);
}
