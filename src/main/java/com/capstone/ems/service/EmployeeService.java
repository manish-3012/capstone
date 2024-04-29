package com.capstone.ems.service;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.dto.EmployeeDto;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    EmployeeEntity save(EmployeeEntity employeeEntity);

    List<EmployeeEntity> findAll();

    Optional<EmployeeEntity> findOne(Long id);

    boolean isExists(Long id);

    EmployeeEntity partialUpdate(Long id, EmployeeEntity employeeEntity);

    void delete(Long id);

    List<EmployeeEntity> findByProjectId(Long projectId);

    List<EmployeeEntity> findByManagerId(Long managerId);
    
    Optional<EmployeeEntity> findByEmail(String email);
    
    Optional<EmployeeEntity> findBySkillsContaining(String skill);

    void assignProjectToEmployee(Long employeeId, Long projectId);
    
    void unassignProjectFromEmployee(Long employeeId);
    
    void requestEmployeeForProject(Long managerId, Long projectId, List<Long> employeeIds);
}