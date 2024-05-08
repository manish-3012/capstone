package com.capstone.ems.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.capstone.ems.domain.dto.UserDto;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.mapper.Mapper;

@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {

	public final ModelMapper modelMapper;
	
	public UserMapperImpl(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	@Override
	public UserDto mapTo(UserEntity userEntity) {
		return modelMapper.map(userEntity, UserDto.class);
	}
	
	@Override
	public UserEntity mapFrom(UserDto requestDto) {
		return modelMapper.map(requestDto, UserEntity.class);
	}
}
