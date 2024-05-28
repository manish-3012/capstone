package com.capstone.ems.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.enums.RequestStatus;
import com.capstone.ems.repository.RequestRepository;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.RequestService;

@Service
public class RequestServiceImpl implements RequestService {
	@Autowired
    private final RequestRepository requestRepository;
    @Autowired
	private final EmployeeService employeeService;

    public RequestServiceImpl(RequestRepository requestRepository,
    		EmployeeService employeeService) {
        this.requestRepository = requestRepository;
        this.employeeService = employeeService;
    }

    @Override
    public RequestEntity save(RequestEntity request) {
        return requestRepository.save(request);
    }
    
    @Override
    public Optional<RequestEntity> getRequestById(Long requestId) {
    	return requestRepository.findById(requestId);
    }

    @Override
    public List<RequestEntity> getRequestsByManager(Long managerId) {
    	EmployeeEntity manager = employeeService.findOne(managerId)
    			.orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));
    	
        return requestRepository.findByManager(manager);
    }

    @Override
    public List<RequestEntity> getRequestsByStatus(RequestStatus status) {
        return requestRepository.findByStatus(status);
    }

    @Override
    public RequestEntity updateRequestStatus(Long requestId, RequestStatus status) {
    	
        RequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(status);
        return requestRepository.save(request);
    }

	@Override
	public List<RequestEntity> findAll() {
		return requestRepository.findAll();
	}
	
	@Override
    public void deleteRequest(Long requestId) {
        if (requestRepository.existsById(requestId)) {
            requestRepository.deleteById(requestId);
        } else {
            throw new RuntimeException("Request not found with id: " + requestId);
        }
    }
}