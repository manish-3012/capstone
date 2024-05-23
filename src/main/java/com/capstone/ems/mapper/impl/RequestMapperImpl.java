package com.capstone.ems.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.capstone.ems.domain.dto.RequestDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.mapper.Mapper;

@Component
public class RequestMapperImpl implements Mapper<RequestEntity, RequestDto> {

    @Override
    public RequestDto mapTo(RequestEntity requestEntity) {
        if (requestEntity == null) {
            return null;
        }

        List<Long> employeeIds = requestEntity.getEmployeeIds() != null ?
                requestEntity.getEmployeeIds() : null;

        return RequestDto.builder()
                .reqId(requestEntity.getReqId())
                .managerId(requestEntity.getManager() != null ? requestEntity.getManager().getEmpId() : null)
                .projectId(requestEntity.getProject() != null ? requestEntity.getProject().getId() : null)
                .employeeIds(employeeIds)
                .status(requestEntity.getStatus())
                .build();
    }

    @Override
    public RequestEntity mapFrom(RequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        EmployeeEntity manager = null;
        if (requestDto.getManagerId() != null) {
            manager = new EmployeeEntity();
            manager.setEmpId(requestDto.getManagerId());
        }

        ProjectEntity project = null;
        if (requestDto.getProjectId() != null) {
            project = new ProjectEntity();
            project.setId(requestDto.getProjectId());
        }

        List<Long> employeeIds = requestDto.getEmployeeIds() != null ?
                requestDto.getEmployeeIds().stream()
                .collect(Collectors.toList()) : null;

        return RequestEntity.builder()
                .reqId(requestDto.getReqId())
                .manager(manager)
                .project(project)
                .employeeIds(employeeIds)
                .status(requestDto.getStatus())
                .build();
    }
}
