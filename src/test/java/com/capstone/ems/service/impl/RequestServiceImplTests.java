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
import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.enums.RequestStatus;
import com.capstone.ems.repository.RequestRepository;
import com.capstone.ems.service.EmployeeService;

class RequestServiceImplTests {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private RequestServiceImpl requestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        RequestEntity requestEntity = new RequestEntity();
        when(requestRepository.save(any(RequestEntity.class))).thenReturn(requestEntity);

        RequestEntity savedRequest = requestService.save(requestEntity);

        assertEquals(requestEntity, savedRequest);
        verify(requestRepository, times(1)).save(any(RequestEntity.class));
    }

    @Test
    void testGetRequestById() {
        Long requestId = 1L;
        RequestEntity requestEntity = new RequestEntity();
        when(requestRepository.findById(eq(requestId))).thenReturn(Optional.of(requestEntity));

        Optional<RequestEntity> foundRequest = requestService.getRequestById(requestId);

        assertEquals(Optional.of(requestEntity), foundRequest);
        verify(requestRepository, times(1)).findById(eq(requestId));
    }

    @Test
    void testGetRequestsByManager() {
        Long managerId = 1L;
        EmployeeEntity manager = new EmployeeEntity();
        List<RequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(new RequestEntity());
        requestEntities.add(new RequestEntity());

        when(employeeService.findOne(eq(managerId))).thenReturn(Optional.of(manager));
        when(requestRepository.findByManager(eq(manager))).thenReturn(requestEntities);

        List<RequestEntity> foundRequests = requestService.getRequestsByManager(managerId);

        assertEquals(2, foundRequests.size());
        verify(employeeService, times(1)).findOne(eq(managerId));
        verify(requestRepository, times(1)).findByManager(eq(manager));
    }

    @Test
    void testGetRequestsByStatus() {
        RequestStatus status = RequestStatus.PENDING;
        List<RequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(new RequestEntity());
        requestEntities.add(new RequestEntity());

        when(requestRepository.findByStatus(eq(status))).thenReturn(requestEntities);

        List<RequestEntity> foundRequests = requestService.getRequestsByStatus(status);

        assertEquals(2, foundRequests.size());
        verify(requestRepository, times(1)).findByStatus(eq(status));
    }

    @Test
    void testUpdateRequestStatus() {
        Long requestId = 1L;
        RequestStatus newStatus = RequestStatus.APPROVED;
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setStatus(RequestStatus.PENDING);

        when(requestRepository.findById(eq(requestId))).thenReturn(Optional.of(requestEntity));
        when(requestRepository.save(any(RequestEntity.class))).thenReturn(requestEntity);

        RequestEntity updatedRequest = requestService.updateRequestStatus(requestId, newStatus);

        assertEquals(newStatus, updatedRequest.getStatus());
        verify(requestRepository, times(1)).findById(eq(requestId));
        verify(requestRepository, times(1)).save(any(RequestEntity.class));
    }

    @Test
    void testUpdateRequestStatus_RequestNotFound() {
        Long requestId = 1L;
        RequestStatus newStatus = RequestStatus.APPROVED;

        when(requestRepository.findById(eq(requestId))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> requestService.updateRequestStatus(requestId, newStatus));
        verify(requestRepository, times(1)).findById(eq(requestId));
        verify(requestRepository, times(0)).save(any(RequestEntity.class));
    }

    @Test
    void testFindAll() {
        List<RequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(new RequestEntity());
        requestEntities.add(new RequestEntity());

        when(requestRepository.findAll()).thenReturn(requestEntities);

        List<RequestEntity> foundRequests = requestService.findAll();

        assertEquals(2, foundRequests.size());
        verify(requestRepository, times(1)).findAll();
    }

    @Test
    void testDeleteRequest() {
        Long requestId = 1L;
        when(requestRepository.existsById(eq(requestId))).thenReturn(true);
        doNothing().when(requestRepository).deleteById(eq(requestId));

        requestService.deleteRequest(requestId);

        verify(requestRepository, times(1)).existsById(eq(requestId));
        verify(requestRepository, times(1)).deleteById(eq(requestId));
    }

    @Test
    void testDeleteRequest_RequestNotFound() {
        Long requestId = 1L;
        when(requestRepository.existsById(eq(requestId))).thenReturn(false);

        assertThrows(RuntimeException.class, () -> requestService.deleteRequest(requestId));
        verify(requestRepository, times(1)).existsById(eq(requestId));
        verify(requestRepository, times(0)).deleteById(eq(requestId));
    }
}