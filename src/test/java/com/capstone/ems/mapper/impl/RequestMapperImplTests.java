package com.capstone.ems.mapper.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.capstone.ems.domain.dto.RequestDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.enums.RequestStatus;

class RequestMapperImplTests {

    private RequestMapperImpl requestMapper;

    @BeforeEach
    void setUp() {
        requestMapper = new RequestMapperImpl();
    }

    @Test
    void testMapToWithNullInput() {
        assertNull(requestMapper.mapTo(null));
    }

    @Test
    void testMapToWithCompleteData() {
        RequestEntity requestEntity = createRequestEntity();

        RequestDto requestDto = requestMapper.mapTo(requestEntity);

        assertNotNull(requestDto);
        assertEquals(1L, requestDto.getReqId());
        assertEquals(2L, requestDto.getManagerId());
        assertEquals(3L, requestDto.getProjectId());
        assertEquals(Arrays.asList(4L, 5L), requestDto.getEmployeeIds());
        assertEquals(RequestStatus.APPROVED, requestDto.getStatus());
    }

    @Test
    void testMapFromWithNullInput() {
        assertNull(requestMapper.mapFrom(null));
    }

    @Test
    void testMapFromWithCompleteData() {
        RequestDto requestDto = createRequestDto();

        RequestEntity requestEntity = requestMapper.mapFrom(requestDto);

        assertNotNull(requestEntity);
        assertEquals(1L, requestEntity.getReqId());
        assertNotNull(requestEntity.getManager());
        assertEquals(2L, requestEntity.getManager().getEmpId());
        assertNotNull(requestEntity.getProject());
        assertEquals(3L, requestEntity.getProject().getId());
        assertEquals(Arrays.asList(4L, 5L), requestEntity.getEmployeeIds());
        assertEquals(RequestStatus.APPROVED, requestEntity.getStatus());
    }

    private RequestEntity createRequestEntity() {
        EmployeeEntity manager = new EmployeeEntity();
        manager.setEmpId(2L);

        ProjectEntity project = new ProjectEntity();
        project.setId(3L);

        List<Long> employeeIds = Arrays.asList(4L, 5L);

        return RequestEntity.builder()
                .reqId(1L)
                .manager(manager)
                .project(project)
                .employeeIds(employeeIds)
                .status(RequestStatus.APPROVED)
                .build();
    }

    private RequestDto createRequestDto() {
        return RequestDto.builder()
                .reqId(1L)
                .managerId(2L)
                .projectId(3L)
                .employeeIds(Arrays.asList(4L, 5L))
                .status(RequestStatus.APPROVED)
                .build();
    }
}