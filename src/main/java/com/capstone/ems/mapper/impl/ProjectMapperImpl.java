package com.capstone.ems.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.capstone.ems.domain.dto.ProjectDto;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.mapper.Mapper;

@Component
public class ProjectMapperImpl implements Mapper<ProjectEntity, ProjectDto> {
	
	private final ModelMapper modelMapper;

    public ProjectMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProjectDto mapTo(ProjectEntity projectEntity) {
        return modelMapper.map(projectEntity, ProjectDto.class);
    }

    @Override
    public ProjectEntity mapFrom(ProjectDto projectDto) {
        return modelMapper.map(projectDto, ProjectEntity.class);
    }
}
