package com.capstone.ems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.capstone.ems.repository.UserRepository;

@Service
public class UserEntityDetailService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).orElseThrow();
    }
	
}
