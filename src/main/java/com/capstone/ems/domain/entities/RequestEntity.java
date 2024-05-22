package com.capstone.ems.domain.entities;
import java.util.List;

import com.capstone.ems.enums.RequestStatus;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

    @ManyToOne
    @JoinColumn(name = "managerId", referencedColumnName = "emp_id")
    private EmployeeEntity manager;

    @ManyToOne
    @JoinColumn(name = "projectId", referencedColumnName = "id")
    private ProjectEntity project;
    
    @ElementCollection
    @CollectionTable(name = "request_employee_ids", joinColumns = @JoinColumn(name = "req_id"))
    @Column(name = "employee_id")
    private List<Long> employeeIds;
    
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
