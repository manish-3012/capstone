package com.capstone.ems.domain.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.capstone.ems.enums.RequestStatus;

public class RequestDtoTest {

    @Test
    public void testGettersAndSetters() {
        Long reqId = 1L;
        Long managerId = 2L;
        Long projectId = 3L;
        List<Long> employeeIds = Arrays.asList(4L, 5L);
        RequestStatus status = RequestStatus.PENDING;

        RequestDto requestDto = new RequestDto();

        requestDto.setReqId(reqId);
        requestDto.setManagerId(managerId);
        requestDto.setProjectId(projectId);
        requestDto.setEmployeeIds(employeeIds);
        requestDto.setStatus(status);

        assertEquals(reqId, requestDto.getReqId());
        assertEquals(managerId, requestDto.getManagerId());
        assertEquals(projectId, requestDto.getProjectId());
        assertEquals(employeeIds, requestDto.getEmployeeIds());
        assertEquals(status, requestDto.getStatus());
    }

    @Test
    public void testBuilder() {
        Long reqId = 1L;
        Long managerId = 2L;
        Long projectId = 3L;
        List<Long> employeeIds = Arrays.asList(4L, 5L);
        RequestStatus status = RequestStatus.PENDING;

        RequestDto requestDto = RequestDto.builder()
                .reqId(reqId)
                .managerId(managerId)
                .projectId(projectId)
                .employeeIds(employeeIds)
                .status(status)
                .build();

        assertEquals(reqId, requestDto.getReqId());
        assertEquals(managerId, requestDto.getManagerId());
        assertEquals(projectId, requestDto.getProjectId());
        assertEquals(employeeIds, requestDto.getEmployeeIds());
        assertEquals(status, requestDto.getStatus());
    }

    @Test
    public void testNoArgsConstructor() {
        RequestDto requestDto = new RequestDto();

        assertNull(requestDto.getReqId());
        assertNull(requestDto.getManagerId());
        assertNull(requestDto.getProjectId());
        assertNull(requestDto.getEmployeeIds());
        assertNull(requestDto.getStatus());
    }
}