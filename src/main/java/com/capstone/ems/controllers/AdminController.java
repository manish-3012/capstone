package com.capstone.ems.controllers;

//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.capstone.ems.domain.dto.EmployeeDto;
//import com.capstone.ems.domain.dto.ProjectDto;
//import com.capstone.ems.domain.dto.RequestDto;
//import com.capstone.ems.domain.dto.UserDto;
//import com.capstone.ems.domain.entities.EmployeeEntity;
//import com.capstone.ems.domain.entities.ProjectEntity;
//import com.capstone.ems.domain.entities.RequestEntity;
//import com.capstone.ems.domain.entities.UserEntity;
//import com.capstone.ems.enums.RequestStatus;
//import com.capstone.ems.enums.UserType;
//import com.capstone.ems.mapper.Mapper;
//import com.capstone.ems.service.EmployeeService;
//import com.capstone.ems.service.ProjectService;
//import com.capstone.ems.service.RequestService;
//import com.capstone.ems.service.UserService;
//
//@RestController
//@RequestMapping("/admin")
public class AdminController {
	
//	@Autowired
//    private EmployeeService employeeService;
//    
//    @Autowired
//    private Mapper<EmployeeEntity, EmployeeDto> employeeMapper;
//    
//    @Autowired
//    private Mapper<UserEntity, UserDto> userMapper;
//    
//    @Autowired
//    private UserService userService;
//    
//    @Autowired
//    private RequestService requestService;
//    
//    @Autowired
//    private Mapper<RequestEntity, RequestDto> requestMapper;
//    
//    @Autowired
//    private Mapper<ProjectEntity, ProjectDto> projectMapper;
//    
//    @Autowired
//    private ProjectService projectService;
//    
//    @PutMapping("/{userId}/type/{userType}")
//    public ResponseEntity<UserDto> updateUserType(@PathVariable Long userId, @PathVariable UserType userType) {
//        userService.updateUserType(userId, userType);
//        UserEntity user = userService.getUserById(userId);
//        return ResponseEntity.ok(userMapper.mapTo(user));
//    }

//    @PutMapping("request/{requestId}/status/{status}")
//    public ResponseEntity<String> updateRequestStatus(@PathVariable Long requestId, @PathVariable RequestStatus status) {
//    	RequestEntity updatedRequestEntity = requestService.updateRequestStatus(requestId, status);
//        return ResponseEntity.ok("Request Status Updated to: " + updatedRequestEntity.getStatus().toString());
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
//        employeeService.delete(id);
//        userService.delete(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
    
//    @PutMapping("/employees/{id}")
//    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
//    	if (!employeeService.isExists(id)) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//    	employeeDto.setEmpId(id);
//        EmployeeEntity employeeEntity = employeeMapper.mapFrom(employeeDto);
//        
//        EmployeeEntity updatedEmployeeEntity = employeeService.save(employeeEntity);
//        return new ResponseEntity<>(employeeMapper.mapTo(updatedEmployeeEntity), HttpStatus.OK);
//    }
//
//    @PatchMapping("/employees/{id}")
//    public ResponseEntity<EmployeeDto> partialUpdateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
//        if (!employeeService.isExists(id)) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        employeeDto.setEmpId(id);
//        EmployeeEntity employeeEntity = employeeMapper.mapFrom(employeeDto);
//        EmployeeEntity updatedEmployeeEntity = employeeService.partialUpdate(id, employeeEntity);
//        return new ResponseEntity<>(employeeMapper.mapTo(updatedEmployeeEntity), HttpStatus.OK);
//    }
    
//    @GetMapping("/manager/{managerId}")
//    public ResponseEntity<List<RequestDto>> getRequestsByManager(@PathVariable Long managerId) {
//        List<RequestEntity> requests = requestService.getRequestsByManager(managerId);
//        List<RequestDto> requestDtos = requests.stream()
//                .map(requestMapper::mapTo)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(requestDtos);
//    }
}
