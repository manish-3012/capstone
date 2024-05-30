package com.capstone.ems.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.capstone.ems.enums.RequestStatus;

class RequestEntityTests {

    @Test
    void testSetAndGetStatus() {
        RequestEntity request = new RequestEntity();

        request.setStatus(RequestStatus.PENDING);

        assertEquals(RequestStatus.PENDING, request.getStatus());
    }

    @Test
    void testSetAndGetManager() {
        RequestEntity request = new RequestEntity();
        EmployeeEntity manager = new EmployeeEntity();

        request.setManager(manager);

        assertEquals(manager, request.getManager());
    }

    @Test
    void testSetAndGetProject() {
        RequestEntity request = new RequestEntity();
        ProjectEntity project = new ProjectEntity();

        request.setProject(project);

        assertEquals(project, request.getProject());
    }

    @Test
    void testSetAndGetEmployeeIds() {
        RequestEntity request = new RequestEntity();
        List<Long> employeeIds = new ArrayList<>();
        employeeIds.add(1L);
        employeeIds.add(2L);

        request.setEmployeeIds(employeeIds);

        assertEquals(employeeIds, request.getEmployeeIds());
    }
}