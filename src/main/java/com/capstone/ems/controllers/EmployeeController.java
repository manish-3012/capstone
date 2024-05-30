package com.capstone.ems.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.ems.domain.dto.EmployeeDto;
import com.capstone.ems.domain.dto.ProjectDto;
import com.capstone.ems.domain.dto.ReqRes;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.mapper.Mapper;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.UserManagementService;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;
    private final Mapper<EmployeeEntity, EmployeeDto> employeeMapper;
    
    @Autowired
    private UserManagementService userManagementService;

    public EmployeeController(EmployeeService employeeService, 
    		Mapper<EmployeeEntity, EmployeeDto> employeeMapper,
    		Mapper<ProjectEntity, ProjectDto> projectMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity = employeeMapper.mapFrom(employeeDto);
        EmployeeEntity savedEmployeeEntity = employeeService.save(employeeEntity);
        return new ResponseEntity<>(employeeMapper.mapTo(savedEmployeeEntity), HttpStatus.CREATED);
    }

    @GetMapping("/all/get-employees")
    public ResponseEntity<List<EmployeeDto>> listEmployees() {
        List<EmployeeEntity> employees = employeeService.findAll();
        return ResponseEntity.ok(employees.stream()
                .map(employeeMapper::mapTo)
                .collect(Collectors.toList()));
    }
    
    @GetMapping("/all/get-employees/project/{id}")
    public List<EmployeeDto> getEmployeesByProjectId(@PathVariable Long projectId) {
    	List<EmployeeEntity> employees = employeeService.findByProject(projectId);
        return employees.stream()
                .map(employeeMapper::mapTo)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/all/get-employee/user-id/{userId}")
    public ResponseEntity<EmployeeDto> getEmployeeByUserId(@PathVariable Long userId) {
    	ReqRes res = userManagementService.getUsersById(userId);
    	UserEntity userEntity = res.getOurUsers();
    	
        Optional<EmployeeEntity> foundEmployee = employeeService.findByUser(userEntity);
        return foundEmployee.map(employeeEntity -> new ResponseEntity<>(employeeMapper.mapTo(employeeEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/all/get-employees/manager/{id}")
    public List<EmployeeDto> getEmployeesByManagerId(@PathVariable Long id) {
    	List<EmployeeEntity> employees = employeeService.findByManager(id);
        return employees.stream()
                .map(employeeMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping("/all/get-employee/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        Optional<EmployeeEntity> foundEmployee = employeeService.findOne(id);
        return foundEmployee.map(employeeEntity -> new ResponseEntity<>(employeeMapper.mapTo(employeeEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/all/get-employee/email/{email}")
    public ResponseEntity<EmployeeDto> getEmployeeByEmail(@PathVariable String email) {
        Optional<EmployeeEntity> foundEmployee = employeeService.findByEmail(email);
        return foundEmployee.map(employeeEntity -> new ResponseEntity<>(employeeMapper.mapTo(employeeEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/all/get-employee/username/{username}")
    public ResponseEntity<EmployeeDto> getEmployeeByUserName(@PathVariable String username) {
        Optional<EmployeeEntity> foundEmployee = employeeService.findByUserName(username);
        return foundEmployee.map(employeeEntity -> new ResponseEntity<>(employeeMapper.mapTo(employeeEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/adminmanager/get-employees/skills/{skill}")
    public ResponseEntity<List<EmployeeEntity>> getEmployeesBySkill(@PathVariable String skill) {
        List<EmployeeEntity> employees = employeeService.findAllBySkillsContaining(skill);
        return ResponseEntity.ok(employees);
    }
    
    @PatchMapping("manageruser/add-skills/{id}")
    public ResponseEntity<EmployeeDto> addSkills(@PathVariable Long id, @RequestBody List<String> skills) {
        Optional<EmployeeEntity> optionalEmployee = employeeService.findOne(id);
        
        if (optionalEmployee.isPresent()) {
            EmployeeEntity employee = optionalEmployee.get();
            
            List<String> existingSkills = employee.getSkills();
            existingSkills.addAll(skills);
            employee.setSkills(existingSkills);
            
            EmployeeEntity updatedEmployee = employeeService.save(employee);
            
            return ResponseEntity.ok(employeeMapper.mapTo(updatedEmployee));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("admin/assign-project/{employeeId}/{projectId}")
    public ResponseEntity<String> assignProject(@PathVariable Long employeeId, @PathVariable Long projectId) {
    	employeeService.assignProjectToEmployee(employeeId, projectId);
        return ResponseEntity.ok("Project assigned to employee successfully");
    }

    @PutMapping("admin/unassign-project/{employeeId}/{projectId}")
    public ResponseEntity<String> unassignProject(@PathVariable Long employeeId, @PathVariable Long projectId) {
    	employeeService.unassignProjectFromEmployee(employeeId);
        return ResponseEntity.ok("Project unassigned from employee successfully");
    }
    
    @DeleteMapping("admin/delete-employee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        userManagementService.deleteUser(id);
        return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
    }
    
    @PutMapping("admin/update-employee/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
    	if (!employeeService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    	
        employeeDto.setEmpId(id);
        EmployeeEntity employeeEntity = employeeMapper.mapFrom(employeeDto);
            
        EmployeeEntity updatedEmployeeEntity = employeeService.save(employeeEntity);
        return new ResponseEntity<>(employeeMapper.mapTo(updatedEmployeeEntity), HttpStatus.OK);
    }

    @PatchMapping("admin/partial-update-employee/{empId}")
    public ResponseEntity<EmployeeDto> partialUpdateEmployee(@PathVariable Long empId, @RequestBody EmployeeDto employeeDto) {
        if (!employeeService.isExists(empId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        employeeDto.setEmpId(empId);
        EmployeeEntity employeeEntity = employeeMapper.mapFrom(employeeDto);

        EmployeeEntity updatedEmployeeEntity = employeeService.partialUpdate(empId, employeeEntity);

        return new ResponseEntity<>(employeeMapper.mapTo(updatedEmployeeEntity), HttpStatus.OK);
    }
    
    
}