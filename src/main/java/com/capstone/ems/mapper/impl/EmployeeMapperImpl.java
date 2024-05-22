package com.capstone.ems.mapper.impl;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.capstone.ems.domain.dto.EmployeeDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.mapper.Mapper;

@Component
public class EmployeeMapperImpl implements Mapper<EmployeeEntity, EmployeeDto> {

    @Override
	public EmployeeDto mapTo(EmployeeEntity employeeEntity) {
        if (employeeEntity == null) {
            return null;
        }
        
        List<Long> managedProjectIds = employeeEntity.getManagedProjectIds() != null ?
                employeeEntity.getManagedProjectIds().stream()
                .map(ProjectEntity::getId)
                .collect(Collectors.toList()) : null;

        return EmployeeDto.builder()
                .empId(employeeEntity.getEmpId())
                .username(employeeEntity.getUsername())
                .email(employeeEntity.getEmail())
                .name(employeeEntity.getName())
                .skills(employeeEntity.getSkills())
                .managerId(employeeEntity.getManager() != null ? employeeEntity.getManager().getEmpId() : null)
                .projectId(employeeEntity.getProject() != null ? employeeEntity.getProject().getId() : null)
                .userType(employeeEntity.getUserType())
                .managedProjectIds(managedProjectIds)
                .userId(employeeEntity.getUser() != null ? employeeEntity.getUser().getUserId() : null)
                .build();
    }

    @Override
    public EmployeeEntity mapFrom(EmployeeDto employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }

        EmployeeEntity manager = null;
        if (employeeDTO.getManagerId() != null) {
            manager = new EmployeeEntity();
            manager.setEmpId(employeeDTO.getManagerId());
        }

        ProjectEntity project = null;
        if (employeeDTO.getProjectId() != null) {
            project = new ProjectEntity();
            project.setId(employeeDTO.getProjectId());
        }
        
        List<ProjectEntity> managedProjectIds = null;
        if (employeeDTO.getManagedProjectIds() != null) {
            managedProjectIds = employeeDTO.getManagedProjectIds().stream()
                    .map(id -> {
                        ProjectEntity projectEntity = new ProjectEntity();
                        projectEntity.setId(id);
                        return projectEntity;
                    })
                    .collect(Collectors.toList());
        }

        UserEntity user = null;
        if (employeeDTO.getUserId() != null) {
            user = new UserEntity();
            user.setUserId(employeeDTO.getUserId());
        }

        return EmployeeEntity.builder()
                .empId(employeeDTO.getEmpId())
                .username(employeeDTO.getUsername())
                .email(employeeDTO.getEmail())
                .name(employeeDTO.getName())
                .skills(employeeDTO.getSkills())
                .manager(manager)
                .project(project)
                .managedProjectIds(managedProjectIds)
                .userType(employeeDTO.getUserType())
                .user(user)
                .build();
    }
}