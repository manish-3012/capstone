package com.capstone.ems.domain.entities;

import java.util.List;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    private String name;

    @ElementCollection
    @CollectionTable(name = "employee_skills", joinColumns = @JoinColumn(name = "emp_id"))
    @Column(name = "skill")
    private List<String> skills;

    @Column(name = "mgr_id")
    private Long managerId;

    @Column(name = "project_id")
    private Long projectId;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN,
        EMPLOYEE,
        MANAGER
    }
}
