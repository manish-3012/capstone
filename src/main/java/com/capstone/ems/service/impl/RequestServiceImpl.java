package com.capstone.ems.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.enums.RequestStatus;
import com.capstone.ems.repository.RequestRepository;
import com.capstone.ems.service.RequestService;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
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
        return requestRepository.findByManagerId(managerId);
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
}