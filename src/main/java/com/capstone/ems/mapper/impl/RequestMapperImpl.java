package com.capstone.ems.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.capstone.ems.domain.dto.RequestDto;
import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.mapper.Mapper;

@Component
public class RequestMapperImpl implements Mapper<RequestEntity, RequestDto> {

	public final ModelMapper modelMapper;
	
	public RequestMapperImpl(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	@Override
	public RequestDto mapTo(RequestEntity requestEntity) {
		return modelMapper.map(requestEntity, RequestDto.class);
	}
	
	@Override
	public RequestEntity mapFrom(RequestDto requestDto) {
		return modelMapper.map(requestDto, RequestEntity.class);
	}
}
