package com.capstone.ems.mapper.impl;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.capstone.ems.domain.dto.EmployeeDto;
import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.mapper.Mapper;

@Component
public class EmployeeMapperImpl implements Mapper<EmployeeEntity, EmployeeDto> {

    private final ModelMapper modelMapper;

    public EmployeeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EmployeeDto mapTo(EmployeeEntity employeeEntity) {
        return modelMapper.map(employeeEntity, EmployeeDto.class);
    }

    @Override
    public EmployeeEntity mapFrom(EmployeeDto employeeDto) {
        return modelMapper.map(employeeDto, EmployeeEntity.class);
    }

}