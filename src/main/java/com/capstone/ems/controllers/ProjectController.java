package com.capstone.ems.controllers;

//import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.ems.domain.dto.ProjectDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.enums.UserType;
import com.capstone.ems.mapper.Mapper;
import com.capstone.ems.service.EmployeeService;
import com.capstone.ems.service.ProjectService;

@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;
    private final Mapper<ProjectEntity, ProjectDto> mapper;

    public ProjectController(ProjectService projectService, 
    		Mapper<ProjectEntity, ProjectDto> projectMapper,
    		EmployeeService employeeService) {
        this.projectService = projectService;
        this.mapper = projectMapper;
        this.employeeService = employeeService;
    }

    @PostMapping("/adminmanager/create-project")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        ProjectEntity projectEntity = mapper.mapFrom(projectDto);
        System.out.println("Project Entity created: " + projectEntity);
        ProjectEntity savedProjectEntity = projectService.save(projectEntity);
        
        System.out.println("Project Entity saved: " + savedProjectEntity);
        return new ResponseEntity<>(mapper.mapTo(savedProjectEntity), HttpStatus.CREATED);
    }

    @GetMapping("/admin/get-projects")
    public List<ProjectDto> listProjects() {
        List<ProjectEntity> projects = projectService.findAll();
        return projects.stream()
                .map(mapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping("/admin/get-project/{id}")
    public ResponseEntity<ProjectDto> adminGetProjectById(@PathVariable Long id) {
        Optional<ProjectEntity> foundProject = projectService.findOne(id);
        return foundProject.map(projectEntity -> new ResponseEntity<>(mapper.mapTo(projectEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/manageruser/get-project/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        Optional<ProjectEntity> foundProject = projectService.findOne(id);
        ProjectEntity project = foundProject.orElseThrow(() -> new RuntimeException("Project not found"));
    
        ProjectDto projectDto = mapper.mapTo(project);
        return ResponseEntity.ok(projectDto);
    }

    @PutMapping("/admin/update-project/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        if (!projectService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        projectDto.setId(id);
        ProjectEntity projectEntity = mapper.mapFrom(projectDto);
        ProjectEntity updatedProjectEntity = projectService.partialUpdate(id, projectEntity);
        return new ResponseEntity<>(mapper.mapTo(updatedProjectEntity), HttpStatus.OK);
    }

    @PatchMapping("/admin/partial-update-project/{id}")
    public ResponseEntity<ProjectDto> partialUpdateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        if (!projectService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        projectDto.setId(id);
        ProjectEntity projectEntity = mapper.mapFrom(projectDto);
        ProjectEntity updatedProjectEntity = projectService.partialUpdate(id, projectEntity);
        return new ResponseEntity<>(mapper.mapTo(updatedProjectEntity), HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete-project/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/adminmanager/get-project/manager/{empId}")
    public ResponseEntity<List<ProjectDto>> adminGetProjectsByManagerId(@PathVariable Long empId) {
        Optional<EmployeeEntity> optionalEmployee = employeeService.findOne(empId);
        
        if (optionalEmployee.isPresent()) {
            EmployeeEntity employee = optionalEmployee.get();
            
            if (employee.getUserType() != UserType.MANAGER) {
                return ResponseEntity.badRequest().build();
            }
            
            List<ProjectEntity> managedProjects = employee.getManagedProjectIds();
            
            List<ProjectDto> projectDtos = managedProjects.stream()
                    .map(mapper::mapTo)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(projectDtos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/manager/get-projects")
    public ResponseEntity<List<ProjectDto>> getProjectsForManager2() {
        EmployeeEntity emp = employeeService.getAuthenticatedEmployee();
        
        Optional<EmployeeEntity> optionalEmployee = employeeService.findOne(emp.getEmpId());
        
        if (optionalEmployee.isPresent()) {
            EmployeeEntity employee = optionalEmployee.get();
            
            if (employee.getUserType() != UserType.MANAGER) {
                return ResponseEntity.badRequest().build();
            }
            
            List<ProjectEntity> managedProjects = employee.getManagedProjectIds();
            
            List<ProjectDto> projectDtos = managedProjects.stream()
                    .map(mapper::mapTo)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(projectDtos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
}