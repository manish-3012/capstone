package com.capstone.ems.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.capstone.ems.domain.dto.ProjectDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.mapper.Mapper;

@Component
public class ProjectMapperImpl implements Mapper<ProjectEntity, ProjectDto> {

    @Override
    public ProjectDto mapTo(ProjectEntity projectEntity) {
        if (projectEntity == null) {
            return null;
        }

        List<Long> employeeIds = projectEntity.getEmployeeIds() != null ?
                projectEntity.getEmployeeIds().stream()
                .map(EmployeeEntity::getEmpId)
                .collect(Collectors.toList()) : null;

        Long managerId = projectEntity.getManager() != null ? projectEntity.getManager().getEmpId() : null;

        return ProjectDto.builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .description(projectEntity.getDescription())
                .skills(projectEntity.getSkills())
                .employeeIds(employeeIds)
                .manager(managerId)
                .build();
    }

    @Override
    public ProjectEntity mapFrom(ProjectDto projectDTO) {
        if (projectDTO == null) {
            return null;
        }

        List<EmployeeEntity> employees = projectDTO.getEmployeeIds() != null ?
                projectDTO.getEmployeeIds().stream()
                .map(empId -> {
                    EmployeeEntity employee = new EmployeeEntity();
                    employee.setEmpId(empId);
                    return employee;
                })
                .collect(Collectors.toList()) : null;

        EmployeeEntity manager = null;
        if (projectDTO.getManager() != null) {
            manager = new EmployeeEntity();
            manager.setEmpId(projectDTO.getManager());
        }

        return ProjectEntity.builder()
                .id(projectDTO.getId())
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .skills(projectDTO.getSkills())
                .employeeIds(employees)
                .manager(manager)
                .build();
    }
}
