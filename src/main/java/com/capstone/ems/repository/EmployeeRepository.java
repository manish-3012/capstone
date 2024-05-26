package com.capstone.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.domain.entities.UserEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
	List<EmployeeEntity> findByProject(ProjectEntity project);
	
	List<EmployeeEntity> findByManager(EmployeeEntity manager);

    Optional<EmployeeEntity> findByEmail(String email);
    
    List<EmployeeEntity> findAllBySkillsContaining(String skill);
    
    Optional<EmployeeEntity> findByUsername(String userName);
    
    Optional<EmployeeEntity> findByEmpId(Long id);
    
    Optional<EmployeeEntity> findByUser(UserEntity user);
}