package com.capstone.ems.service;

import java.util.List;
import java.util.Optional;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.UserEntity;

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
    
    Optional<EmployeeEntity> findByUserName(String userName);
    
    List<EmployeeEntity> findAllBySkillsContaining(String skill);
    
    void assignProjectToEmployee(Long employeeId, Long projectId);
    
    void unassignProjectFromEmployee(Long employeeId);
    
    public EmployeeEntity getAuthenticatedEmployee();
    
//    EmployeeEntity createEmployeeFromUser(UserEntity userEntity);
    
}