package com.capstone.ems.domain.entities;
import java.util.List;

import com.capstone.ems.enums.RequestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reqId;
    
    @NotBlank(message = "Manager ID is required")
    private Long managerId;
    
    @NotBlank(message = "Project ID is required")
    private Long projectId;
    private List<Long> employeeIds;
    private RequestStatus status;
}
