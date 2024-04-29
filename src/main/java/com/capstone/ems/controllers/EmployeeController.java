package com.capstone.ems.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final Mapper<EmployeeEntity, EmployeeDto> mapper;

    public EmployeeController(EmployeeService employeeService, Mapper<EmployeeEntity, EmployeeDto> authorMapper) {
        this.employeeService = employeeService;
        this.mapper = authorMapper;
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity = mapper.mapFrom(employeeDto);
        EmployeeEntity savedEmployeeEntity = employeeService.save(employeeEntity);
        return new ResponseEntity<>(mapper.mapTo(savedEmployeeEntity), HttpStatus.CREATED);
    }

    @GetMapping
    public List<EmployeeDto> listEmployees() {
        List<EmployeeEntity> employees = employeeService.findAll();
        return employees.stream()
                .map(mapper::mapTo)
                .collect(Collectors.toList());
    }
    
    @GetMapping("project_id/{id}")
    public List<EmployeeDto> getEmployeesByProjectId(@PathVariable Long project_id) {
    	List<EmployeeEntity> employees = employeeService.findByProjectId(project_id);
        return employees.stream()
                .map(mapper::mapTo)
                .collect(Collectors.toList());
    }
    
    @GetMapping("mgr_id/{id}")
    public List<EmployeeDto> getEmployeesByManagerId(@PathVariable Long mgr_id) {
    	List<EmployeeEntity> employees = employeeService.findByManagerId(mgr_id);
        return employees.stream()
                .map(mapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        Optional<EmployeeEntity> foundEmployee = employeeService.findOne(id);
        return foundEmployee.map(employeeEntity -> new ResponseEntity<>(mapper.mapTo(employeeEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        if (!employeeService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        employeeDto.setEmpId(id);
        EmployeeEntity employeeEntity = mapper.mapFrom(employeeDto);
        EmployeeEntity updatedEmployeeEntity = employeeService.save(employeeEntity);
        return new ResponseEntity<>(mapper.mapTo(updatedEmployeeEntity), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDto> partialUpdateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        if (!employeeService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        employeeDto.setEmpId(id);
        EmployeeEntity employeeEntity = mapper.mapFrom(employeeDto);
        EmployeeEntity updatedEmployeeEntity = employeeService.partialUpdate(id, employeeEntity);
        return new ResponseEntity<>(mapper.mapTo(updatedEmployeeEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeDto> getEmployeeByEmail(@PathVariable String email) {
        Optional<EmployeeEntity> foundEmployee = employeeService.findByEmail(email);
        return foundEmployee.map(employeeEntity -> new ResponseEntity<>(mapper.mapTo(employeeEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/skills/{skill}")
    public ResponseEntity<EmployeeEntity> getEmployeesBySkill(@PathVariable String skill) {
        Optional<EmployeeEntity> employee = employeeService.findBySkillsContaining(skill);
        return employee.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{employeeId}/projects/{projectId}")
    public ResponseEntity<Void> assignProjectToEmployee(@PathVariable Long employeeId, @PathVariable Long projectId) {
        employeeService.assignProjectToEmployee(employeeId, projectId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{employeeId}/projects")
    public ResponseEntity<Void> unassignProjectFromEmployee(@PathVariable Long employeeId) {
        employeeService.unassignProjectFromEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/request-project")
    public ResponseEntity<Void> requestEmployeeForProject(@RequestParam Long managerId, @RequestParam Long projectId, @RequestBody List<Long> employeeIds) {
        employeeService.requestEmployeeForProject(managerId, projectId, employeeIds);
        return ResponseEntity.noContent().build();
    }
}