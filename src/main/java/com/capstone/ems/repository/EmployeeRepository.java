package com.capstone.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.ems.domain.entities.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findByProjectId(Long projectId);

    List<EmployeeEntity> findByManagerId(Long managerId);
    
    Optional<EmployeeEntity> findByEmail(String email);
    
    Optional<EmployeeEntity> findBySkillsContaining(String skill);
}