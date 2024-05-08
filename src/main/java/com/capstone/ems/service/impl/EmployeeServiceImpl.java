package com.capstone.ems.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.repository.EmployeeRepository;
import com.capstone.ems.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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
    public Optional<EmployeeEntity> findBySkillsContaining(String skill) {
        return employeeRepository.findBySkillsContaining(skill);
    }
    
    @Override
    public void assignProjectToEmployee(Long employeeId, Long projectId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if (employee.getProjectId() != null) {
            throw new RuntimeException("Employee is already assigned to a project");
        }
        
        employee.setProjectId(projectId);
        employeeRepository.save(employee);
    }
    
    @Override
    public void unassignProjectFromEmployee(Long employeeId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setProjectId(null);
        employeeRepository.save(employee);
    }

	@Override
	public Optional<EmployeeEntity> findByUserName(String userName) {
		return employeeRepository.findByUserName(userName);
	}
	
}