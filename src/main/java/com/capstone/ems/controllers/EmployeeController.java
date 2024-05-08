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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.ems.domain.dto.EmployeeDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.mapper.Mapper;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.UserService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final Mapper<EmployeeEntity, EmployeeDto> employeeMapper;
    
    @Autowired
    private UserService userService;

    public EmployeeController(EmployeeService employeeService, Mapper<EmployeeEntity, EmployeeDto> employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity = employeeMapper.mapFrom(employeeDto);
        EmployeeEntity savedEmployeeEntity = employeeService.save(employeeEntity);
        return new ResponseEntity<>(employeeMapper.mapTo(savedEmployeeEntity), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> listEmployees() {
        List<EmployeeEntity> employees = employeeService.findAll();
        return ResponseEntity.ok(employees.stream()
                .map(employeeMapper::mapTo)
                .collect(Collectors.toList()));
    }
    
    @GetMapping("project_id/{id}")
    public List<EmployeeDto> getEmployeesByProjectId(@PathVariable Long project_id) {
    	List<EmployeeEntity> employees = employeeService.findByProjectId(project_id);
        return employees.stream()
                .map(employeeMapper::mapTo)
                .collect(Collectors.toList());
    }
    
    @GetMapping("mgr_id/{id}")
    public List<EmployeeDto> getEmployeesByManagerId(@PathVariable Long mgr_id) {
    	List<EmployeeEntity> employees = employeeService.findByManagerId(mgr_id);
        return employees.stream()
                .map(employeeMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        Optional<EmployeeEntity> foundEmployee = employeeService.findOne(id);
        return foundEmployee.map(employeeEntity -> new ResponseEntity<>(employeeMapper.mapTo(employeeEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    
    
    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeDto> getEmployeeByEmail(@PathVariable String email) {
        Optional<EmployeeEntity> foundEmployee = employeeService.findByEmail(email);
        return foundEmployee.map(employeeEntity -> new ResponseEntity<>(employeeMapper.mapTo(employeeEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<EmployeeDto> getEmployeeByUserName(@PathVariable String username) {
        Optional<EmployeeEntity> foundEmployee = employeeService.findByUserName(username);
        return foundEmployee.map(employeeEntity -> new ResponseEntity<>(employeeMapper.mapTo(employeeEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/skills/{skill}")
    public ResponseEntity<EmployeeEntity> getEmployeesBySkill(@PathVariable String skill) {
        Optional<EmployeeEntity> employee = employeeService.findBySkillsContaining(skill);
        return employee.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/add-skill")
    public ResponseEntity<EmployeeDto> addSkill(@PathVariable Long id, @RequestParam String skill) {
        Optional<EmployeeEntity> optionalEmployee = employeeService.findOne(id);
        
        if (optionalEmployee.isPresent()) {
            EmployeeEntity employee = optionalEmployee.get();
            
            List<String> skills = employee.getSkills();
            skills.add(skill);
            employee.setSkills(skills);
            
            EmployeeEntity updatedEmployee = employeeService.save(employee);
            
            return ResponseEntity.ok(employeeMapper.mapTo(updatedEmployee));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/assign-project/{employeeId}/{projectId}")
    public ResponseEntity<String> assignProject(@PathVariable Long employeeId, @PathVariable Long projectId) {
    	employeeService.assignProjectToEmployee(employeeId, projectId);
        return ResponseEntity.ok("Project assigned to employee successfully");
    }

    // LOOK INTO THIS
    @PutMapping("/unassign-project/{employeeId}/{projectId}")
    public ResponseEntity<String> unassignProject(@PathVariable Long employeeId, @PathVariable Long projectId) {
    	employeeService.unassignProjectFromEmployee(employeeId);
        return ResponseEntity.ok("Project unassigned from employee successfully");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
    	if (!employeeService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    	employeeDto.setEmpId(id);
        EmployeeEntity employeeEntity = employeeMapper.mapFrom(employeeDto);
        
        EmployeeEntity updatedEmployeeEntity = employeeService.save(employeeEntity);
        return new ResponseEntity<>(employeeMapper.mapTo(updatedEmployeeEntity), HttpStatus.OK);
    }

    @PatchMapping("/employees/{id}")
    public ResponseEntity<EmployeeDto> partialUpdateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        if (!employeeService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        employeeDto.setEmpId(id);
        EmployeeEntity employeeEntity = employeeMapper.mapFrom(employeeDto);
        EmployeeEntity updatedEmployeeEntity = employeeService.partialUpdate(id, employeeEntity);
        return new ResponseEntity<>(employeeMapper.mapTo(updatedEmployeeEntity), HttpStatus.OK);
    }
}