package com.capstone.ems.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capstone.ems.domain.dto.ProjectDto;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.mapper.Mapper;
import com.capstone.ems.service.ProjectService;

@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final Mapper<ProjectEntity, ProjectDto> mapper;

    public ProjectController(ProjectService projectService, Mapper<ProjectEntity, ProjectDto> projectMapper) {
        this.projectService = projectService;
        this.mapper = projectMapper;
    }

    @PostMapping("/adminmanager/create-project")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        ProjectEntity projectEntity = mapper.mapFrom(projectDto);
        ProjectEntity savedProjectEntity = projectService.save(projectEntity);
        return new ResponseEntity<>(mapper.mapTo(savedProjectEntity), HttpStatus.CREATED);
    }

    @GetMapping("/adminmanager/get-projects")
    public List<ProjectDto> listProjects() {
        List<ProjectEntity> projects = projectService.findAll();
        return projects.stream()
                .map(mapper::mapTo)
                .collect(Collectors.toList());
    }
    
    

    @GetMapping("/adminmanager/get-project/{id}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long id) {
        Optional<ProjectEntity> foundProject = projectService.findOne(id);
        return foundProject.map(projectEntity -> new ResponseEntity<>(mapper.mapTo(projectEntity), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/adminmanager/update-project/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        if (!projectService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        projectDto.setId(id);
        ProjectEntity projectEntity = mapper.mapFrom(projectDto);
        ProjectEntity updatedProjectEntity = projectService.partialUpdate(id, projectEntity);
        return new ResponseEntity<>(mapper.mapTo(updatedProjectEntity), HttpStatus.OK);
    }

    @PatchMapping("/adminmanager/partial-update-project/{id}")
    public ResponseEntity<ProjectDto> partialUpdateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        if (!projectService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        projectDto.setId(id);
        ProjectEntity projectEntity = mapper.mapFrom(projectDto);
        ProjectEntity updatedProjectEntity = projectService.partialUpdate(id, projectEntity);
        return new ResponseEntity<>(mapper.mapTo(updatedProjectEntity), HttpStatus.OK);
    }

    @DeleteMapping("/adminmanager/delete-project/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/manager/{managerId}")
    public List<ProjectDto> getProjectsByManagerId(@PathVariable Long managerId) {
        List<ProjectEntity> projects = projectService.findProjectsByManagerId(managerId);
        return projects.stream()
                .map(mapper::mapTo)
                .collect(Collectors.toList());
    }
}