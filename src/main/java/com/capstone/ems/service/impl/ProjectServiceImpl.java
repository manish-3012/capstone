package com.capstone.ems.service.impl;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.enums.UserType;
import com.capstone.ems.repository.EmployeeRepository;
import com.capstone.ems.repository.ProjectRepository;
import com.capstone.ems.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
    private final ProjectRepository projectRepository;
	@Autowired
	private final EmployeeRepository employeeRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, 
    		EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ProjectEntity save(ProjectEntity projectEntity) {
    	EmployeeEntity manager = projectEntity.getManager();
        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null for the project.");
        }

        Long managerId = manager.getEmpId();
        if (managerId == null) {
            throw new IllegalArgumentException("Manager ID cannot be null.");
        }
        
        return projectRepository.save(projectEntity);
    }

    @Override
    public List<ProjectEntity> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<ProjectEntity> findOne(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return projectRepository.existsById(id);
    }

    @Override
    public ProjectEntity partialUpdate(Long id, ProjectEntity projectEntity) {
        projectEntity.setId(id);

        return projectRepository.findById(id).map(existingProject -> {
            Optional.ofNullable(projectEntity.getName()).ifPresent(existingProject::setName);
            Optional.ofNullable(projectEntity.getDescription()).ifPresent(existingProject::setDescription);
            Optional.ofNullable(projectEntity.getSkills()).ifPresent(existingProject::setSkills); // Update this line to handle List<String>
            Optional.ofNullable(projectEntity.getManager()).ifPresent(existingProject::setManager);
            return projectRepository.save(existingProject);
        }).orElseThrow(() -> new RuntimeException("Project does not exist"));
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

}