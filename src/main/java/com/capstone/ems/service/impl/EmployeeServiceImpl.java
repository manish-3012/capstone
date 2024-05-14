package com.capstone.ems.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.repository.EmployeeRepository;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.ProjectService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
   
    private final ProjectService projectService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
    		ProjectService projectService) {
        this.employeeRepository = employeeRepository;
        this.projectService = projectService;
    }

    @Override
    public EmployeeEntity save(EmployeeEntity employeeEntity) {
        return employeeRepository.save(employeeEntity);
    }

    @Override
    public List<EmployeeEntity> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<EmployeeEntity> findOne(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return employeeRepository.existsById(id);
    }

    @Override
    public EmployeeEntity partialUpdate(Long id, EmployeeEntity employeeEntity) {
        employeeEntity.setEmpId(id);

        return employeeRepository.findById(id).map(existingEmployee -> {
            Optional.ofNullable(employeeEntity.getName()).ifPresent(existingEmployee::setName);
            Optional.ofNullable(employeeEntity.getSkills()).ifPresent(existingEmployee::setSkills); // Update this line to handle List<String>
            Optional.ofNullable(employeeEntity.getManagerId()).ifPresent(existingEmployee::setManagerId);
            Optional.ofNullable(employeeEntity.getProjectId()).ifPresent(existingEmployee::setProjectId);
            return employeeRepository.save(existingEmployee);
        }).orElseThrow(() -> new RuntimeException("Employee does not exist"));
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

   
    @Override
    public List<EmployeeEntity> findByProjectId(Long projectId) {
        return employeeRepository.findByProjectId(projectId);
    }

    @Override
    public List<EmployeeEntity> findByManagerId(Long managerId) {
        return employeeRepository.findByManagerId(managerId);
    }
    
    @Override
    public Optional<EmployeeEntity> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
    
    @Override
    public List<EmployeeEntity> findAllBySkillsContaining(String skill) {
        return employeeRepository.findAllBySkillsContaining(skill);
    }
    
    @Override
    public void assignProjectToEmployee(Long employeeId, Long projectId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if (employee.getProjectId() != null) {
            throw new RuntimeException("Employee is already assigned to a project");
        }
        
        
        ProjectEntity project = projectService.findOne(projectId)
        		.orElseThrow(() -> new RuntimeException("Project not found"));
        
        employee.setProjectId(projectId);
        employeeRepository.save(employee);
    }
    
    @Override
    public void unassignProjectFromEmployee(Long employeeId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if(employee.getProjectId() == null)
            throw new RuntimeException("No project assigned to this employee");
        
        employee.setProjectId(null);
        employee.setManagerId(null);
        employeeRepository.save(employee);
    }

	@Override
	public Optional<EmployeeEntity> findByUserName(String userName) {
		return employeeRepository.findByUserName(userName);
	}
	
	@Override
	public EmployeeEntity getAuthenticatedEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<EmployeeEntity> foundEmployee = findByEmail(email);
        return foundEmployee.orElseThrow(() -> new RuntimeException("Employee not found"));
    }
	
	@Override
	public void assignManagerToEmployee(Long employeeId, Long managerId) {
	    EmployeeEntity employee = employeeRepository.findById(employeeId)
	            .orElseThrow(() -> new RuntimeException("Employee not found"));
	    
	    if (employee.getManagerId() != null) {
	        throw new RuntimeException("Employee is already assigned to a manager");
	    }
	    
	    employeeRepository.findById(managerId)
	            .orElseThrow(() -> new RuntimeException("Manager not found"));
	    
	    employee.setManagerId(managerId);
	    employeeRepository.save(employee);
	}

}