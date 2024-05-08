package com.capstone.ems.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.repository.UserRepository;

@Service
public class UserEntityDetailService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<UserEntity> user = repository.findByEmail(email);
		if(user.isPresent()) {
			UserEntity userObj = user.get();
			 return User.builder()
		                .username(userObj.getEmail())
		                .password(userObj.getPassword())
		                .roles(userObj.getUserType().toString())
		                .build();
		} else {
			throw new UsernameNotFoundException(email);
		}
	}
	
}
