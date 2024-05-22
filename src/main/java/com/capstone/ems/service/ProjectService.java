package com.capstone.ems.service;

import java.util.List;
import java.util.Optional;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;

public interface ProjectService {
	
	ProjectEntity save(ProjectEntity projectEntity);

    List<ProjectEntity> findAll();

    Optional<ProjectEntity> findOne(Long id);

    boolean isExists(Long id);

    ProjectEntity partialUpdate(Long id, ProjectEntity projectEntity);

    void delete(Long id);
    
//	List<ProjectEntity> findProjectsByManagerId(Long managerId);

}
