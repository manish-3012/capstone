package com.capstone.ems.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.capstone.ems.domain.entities.EmployeeEntity;
import com.capstone.ems.domain.entities.ProjectEntity;
import com.capstone.ems.domain.entities.UserEntity;
import com.capstone.ems.repository.EmployeeRepository;
import com.capstone.ems.service.ProjectService;

class EmployeeServiceImplTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);

        EmployeeEntity savedEmployee = employeeService.save(employeeEntity);

        assertEquals(employeeEntity, savedEmployee);
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void testFindAll() {
        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        employeeEntities.add(new EmployeeEntity());
        employeeEntities.add(new EmployeeEntity());

        when(employeeRepository.findAll()).thenReturn(employeeEntities);

        List<EmployeeEntity> foundEmployees = employeeService.findAll();

        assertEquals(2, foundEmployees.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testFindOne() {
        Long employeeId = 1L;
        EmployeeEntity employeeEntity = new EmployeeEntity();
        when(employeeRepository.findByEmpId(eq(employeeId))).thenReturn(Optional.of(employeeEntity));

        Optional<EmployeeEntity> foundEmployee = employeeService.findOne(employeeId);

        assertEquals(Optional.of(employeeEntity), foundEmployee);
        verify(employeeRepository, times(1)).findByEmpId(eq(employeeId));
    }

    @Test
    void testIsExists() {
        Long employeeId = 1L;
        when(employeeRepository.existsById(eq(employeeId))).thenReturn(true);

        boolean exists = employeeService.isExists(employeeId);

        assertEquals(true, exists);
        verify(employeeRepository, times(1)).existsById(eq(employeeId));
    }

    @Test
    void testPartialUpdate() {
        Long employeeId = 1L;
        EmployeeEntity updatedEmployee = new EmployeeEntity();
        updatedEmployee.setName("Updated Name");

        when(employeeRepository.findByEmpId(eq(employeeId))).thenReturn(Optional.of(new EmployeeEntity()));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(updatedEmployee);

        EmployeeEntity savedEmployee = employeeService.partialUpdate(employeeId, updatedEmployee);

        assertEquals("Updated Name", savedEmployee.getName());
        verify(employeeRepository, times(1)).findByEmpId(eq(employeeId));
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void testDelete() {
        Long employeeId = 1L;
        doNothing().when(employeeRepository).deleteById(eq(employeeId));

        employeeService.delete(employeeId);

        verify(employeeRepository, times(1)).deleteById(eq(employeeId));
    }

    @Test
    void testFindByProject() {
        Long projectId = 1L;
        ProjectEntity projectEntity = new ProjectEntity();
        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        employeeEntities.add(new EmployeeEntity());
        employeeEntities.add(new EmployeeEntity());

        when(projectService.findOne(eq(projectId))).thenReturn(Optional.of(projectEntity));
        when(employeeRepository.findByProject(eq(projectEntity))).thenReturn(employeeEntities);

        List<EmployeeEntity> foundEmployees = employeeService.findByProject(projectId);

        assertEquals(2, foundEmployees.size());
        verify(projectService, times(1)).findOne(eq(projectId));
        verify(employeeRepository, times(1)).findByProject(eq(projectEntity));
    }

    @Test
    void testFindByManager() {
        Long managerId = 1L;
        EmployeeEntity managerEntity = new EmployeeEntity();
        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        employeeEntities.add(new EmployeeEntity());
        employeeEntities.add(new EmployeeEntity());

        when(employeeRepository.findById(eq(managerId))).thenReturn(Optional.of(managerEntity));
        when(employeeRepository.findByManager(eq(managerEntity))).thenReturn(employeeEntities);

        List<EmployeeEntity> foundEmployees = employeeService.findByManager(managerId);

        assertEquals(2, foundEmployees.size());
        verify(employeeRepository, times(1)).findById(eq(managerId));
        verify(employeeRepository, times(1)).findByManager(eq(managerEntity));
    }

    @Test
    void testFindByUser() {
        UserEntity userEntity = new UserEntity();
        EmployeeEntity employeeEntity = new EmployeeEntity();
        when(employeeRepository.findByUser(eq(userEntity))).thenReturn(Optional.of(employeeEntity));

        Optional<EmployeeEntity> foundEmployee = employeeService.findByUser(userEntity);

        assertEquals(Optional.of(employeeEntity), foundEmployee);
        verify(employeeRepository, times(1)).findByUser(eq(userEntity));
    }

    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        EmployeeEntity employeeEntity = new EmployeeEntity();
        when(employeeRepository.findByEmail(eq(email))).thenReturn(Optional.of(employeeEntity));

        Optional<EmployeeEntity> foundEmployee = employeeService.findByEmail(email);

        assertEquals(Optional.of(employeeEntity), foundEmployee);
        verify(employeeRepository, times(1)).findByEmail(eq(email));
    }

    @Test
    void testFindAllBySkillsContaining() {
        String skill = "Java";
        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        employeeEntities.add(new EmployeeEntity());
        employeeEntities.add(new EmployeeEntity());

        when(employeeRepository.findAllBySkillsContaining(eq(skill))).thenReturn(employeeEntities);

        List<EmployeeEntity> foundEmployees = employeeService.findAllBySkillsContaining(skill);

        assertEquals(2, foundEmployees.size());
        verify(employeeRepository, times(1)).findAllBySkillsContaining(eq(skill));
    }

    @Test
    void testAssignProjectToEmployee() {
        Long employeeId = 1L;
        Long projectId = 1L;
        EmployeeEntity employeeEntity = new EmployeeEntity();
        ProjectEntity projectEntity = new ProjectEntity();

        when(employeeRepository.findById(eq(employeeId))).thenReturn(Optional.of(employeeEntity));
        when(projectService.findOne(eq(projectId))).thenReturn(Optional.of(projectEntity));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);

        employeeService.assignProjectToEmployee(employeeId, projectId);

        assertEquals(projectEntity, employeeEntity.getProject());
        verify(employeeRepository, times(1)).findById(eq(employeeId));
        verify(projectService, times(1)).findOne(eq(projectId));
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void testUnassignProjectFromEmployee() {
        Long employeeId = 1L;
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setProject(new ProjectEntity());

        when(employeeRepository.findById(eq(employeeId))).thenReturn(Optional.of(employeeEntity));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);

        employeeService.unassignProjectFromEmployee(employeeId);

        assertEquals(null, employeeEntity.getProject());
        verify(employeeRepository, times(1)).findById(eq(employeeId));
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void testFindByUserName() {
        String userName = "testuser";
        EmployeeEntity employeeEntity = new EmployeeEntity();
        when(employeeRepository.findByUsername(eq(userName))).thenReturn(Optional.of(employeeEntity));

        Optional<EmployeeEntity> foundEmployee = employeeService.findByUserName(userName);

        assertEquals(Optional.of(employeeEntity), foundEmployee);
        verify(employeeRepository, times(1)).findByUsername(eq(userName));
    }

    @Test
    void testGetAuthenticatedEmployee() {
        String email = "test@example.com";
        EmployeeEntity employeeEntity = new EmployeeEntity();
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(employeeRepository.findByEmail(eq(email))).thenReturn(Optional.of(employeeEntity));
        SecurityContextHolder.setContext(securityContext);

        EmployeeEntity authenticatedEmployee = employeeService.getAuthenticatedEmployee();

        assertEquals(employeeEntity, authenticatedEmployee);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(employeeRepository, times(1)).findByEmail(eq(email));
    }

    @Test
    void testAssignManagerToEmployee() {
        Long employeeId = 1L;
        Long managerId = 2L;
        EmployeeEntity employeeEntity = new EmployeeEntity();
        EmployeeEntity managerEntity = new EmployeeEntity();

        when(employeeRepository.findByEmpId(eq(employeeId))).thenReturn(Optional.of(employeeEntity));
        when(employeeRepository.findByEmpId(eq(managerId))).thenReturn(Optional.of(managerEntity));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);

        employeeService.assignManagerToEmployee(employeeId, managerId);

        assertEquals(managerEntity, employeeEntity.getManager());
        verify(employeeRepository, times(1)).findByEmpId(eq(employeeId));
        verify(employeeRepository, times(1)).findByEmpId(eq(managerId));
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void testAssignManagerToEmployee_EmployeeAlreadyAssigned() {
        Long employeeId = 1L;
        Long managerId = 2L;
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setManager(new EmployeeEntity());

        when(employeeRepository.findByEmpId(eq(employeeId))).thenReturn(Optional.of(employeeEntity));

        assertThrows(RuntimeException.class, () -> employeeService.assignManagerToEmployee(employeeId, managerId));
        verify(employeeRepository, times(1)).findByEmpId(eq(employeeId));
    }
}