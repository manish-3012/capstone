package com.capstone.ems.domain.entities;

import java.util.List;

import com.capstone.ems.enums.UserType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "emp_id")
    private Long empId;

    @NotNull
    @Column(name = "userName")
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Column(name = "email", unique = true)
    @Email
    private String email;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_skills", joinColumns = @JoinColumn(name = "emp_id"))
    @Column(name = "skill")
    private List<String> skills;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "emp_id")
    private EmployeeEntity manager;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProjectEntity> managedProjectIds;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", referencedColumnName = "id", unique = true)
    private ProjectEntity project;

    @Enumerated(EnumType.STRING)
    private UserType userType;
    
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private UserEntity user;
}
