package com.capstone.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.RequestEntity;
import com.capstone.ems.enums.RequestStatus;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
	List<RequestEntity> findByManager(EmployeeEntity manager);
	List<RequestEntity> findByStatus(RequestStatus status);
}
