package com.capstone.ems.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.repository.EmployeeRepository;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.ProjectService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
    private EmployeeRepository employeeRepository;
	@Autowired
	private ProjectService projectService; 

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
        Optional<EmployeeEntity> employee = employeeRepository.findByEmpId(id);
        return employee;
    }


    @Override
    public boolean isExists(Long id) {
        return employeeRepository.existsById(id);
    }

    @Override
    public EmployeeEntity partialUpdate(Long empId, EmployeeEntity employeeEntity) {
        employeeEntity.setEmpId(empId);

        return employeeRepository.findByEmpId(empId).map(existingEmployee -> {
            Optional.ofNullable(employeeEntity.getName()).ifPresent(existingEmployee::setName);
            Optional.ofNullable(employeeEntity.getEmail()).ifPresent(existingEmployee::setEmail);
            Optional.ofNullable(employeeEntity.getSkills()).ifPresent(existingEmployee::setSkills); // Update this line to handle List<String>
            Optional.ofNullable(employeeEntity.getManager()).ifPresent(existingEmployee::setManager);
            Optional.ofNullable(employeeEntity.getProject()).ifPresent(existingEmployee::setProject);
            Optional.ofNullable(employeeEntity.getUserType()).ifPresent(existingEmployee::setUserType);
            Optional.ofNullable(employeeEntity.getUsername()).ifPresent(existingEmployee::setUsername);
            Optional.ofNullable(employeeEntity.getManagedProjectIds()).ifPresent(managedProjectIds -> {
                existingEmployee.getManagedProjectIds().clear();
                existingEmployee.getManagedProjectIds().addAll(managedProjectIds);
            });
            return employeeRepository.save(existingEmployee);
        }).orElseThrow(() -> new RuntimeException("Employee does not exist"));
    }

    @Override
    public void delete(Long empId) {
        employeeRepository.deleteById(empId);
    }

   
    @Override
    public List<EmployeeEntity> findByProject(Long projectId) {
    	ProjectEntity project = projectService.findOne(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        
        return employeeRepository.findByProject(project);
    }

    @Override
    public List<EmployeeEntity> findByManager(Long managerId) {
    	EmployeeEntity manager = employeeRepository.findById(managerId)
	            .orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));
	    
        return employeeRepository.findByManager(manager);
    }
    
    @Override
    public Optional<EmployeeEntity> findByUser(UserEntity userEntity) {
      
    	return employeeRepository.findByUser(userEntity);
    }
    
    @Override
    public Optional<EmployeeEntity> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
    
    @Override
    public List<EmployeeEntity> findAllBySkillsContaining(String skill) {
        return employeeRepository.findAllBySkillsContaining(skill);
    }
    
    public void assignProjectToEmployee(Long employeeId, Long projectId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        ProjectEntity project = projectService.findOne(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        
        if (employee.getProject() != null) {
            throw new RuntimeException("Employee is already assigned to a project");
        }
        
        employee.setProject(project);
        employeeRepository.save(employee);
    }
    
    @Override
    public void unassignProjectFromEmployee(Long employeeId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        if (employee.getProject() == null) {
            throw new RuntimeException("No project assigned to this employee");
        }
        
        employee.setProject(null);
        employeeRepository.save(employee);
    }

	@Override
	public Optional<EmployeeEntity> findByUserName(String userName) {
		return employeeRepository.findByUsername(userName);
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
	    EmployeeEntity employee = employeeRepository.findByEmpId(employeeId)
	            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
	    
	    EmployeeEntity manager = employeeRepository.findByEmpId(managerId)
	            .orElseThrow(() -> new RuntimeException("Manager not found with id: " + managerId));
	    
	    if (employee.getManager() != null) {
	        throw new RuntimeException("Employee is already assigned to a manager");
	    }
	    
	    employee.setManager(manager);
	    employeeRepository.save(employee);
	}

}