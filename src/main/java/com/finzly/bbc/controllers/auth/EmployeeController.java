package com.finzly.bbc.controllers.auth;

import com.finzly.bbc.dto.auth.EmployeeDTO;
import com.finzly.bbc.dto.auth.UserEmployeeCreationDTO;
import com.finzly.bbc.dto.auth.UserEmployeeSearchDTO;
import com.finzly.bbc.dto.auth.mapper.EmployeeMapper;
import com.finzly.bbc.dto.auth.mapper.UserEmployeeCreationMapper;
import com.finzly.bbc.models.auth.Employee;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.services.auth.EmployeeService;
import com.finzly.bbc.services.auth.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/employees")
@Tag(name = "Employees API", description = "Employees API for managing employees. Create, read, update, delete employees. Get employee by ID.")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee (@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity (employeeDTO);
        Employee createdEmployee = employeeService.createEmployee (employee);
        return ResponseEntity.ok (EmployeeMapper.toDTO (createdEmployee));
    }

    @PostMapping("/create-with-user")
    public ResponseEntity<EmployeeDTO> createEmployeeWithUserDetails (@RequestBody UserEmployeeCreationDTO userEmployeeCreationDTO) {
        User user = UserEmployeeCreationMapper.toUser (userEmployeeCreationDTO);
        User createdUser = userService.createUser (user);
        Employee employee = UserEmployeeCreationMapper.toEmployee (userEmployeeCreationDTO, createdUser);
        Employee createdEmployee = employeeService.createEmployee (employee);
        return ResponseEntity.ok (EmployeeMapper.toDTO (createdEmployee));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById (@PathVariable String employeeId) {
        Employee employee = employeeService.getEmployeeById (employeeId);
        return ResponseEntity.ok (EmployeeMapper.toDTO (employee));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees () {
        List<Employee> employees = employeeService.getAllEmployees ();
        List<EmployeeDTO> employeeDTOs = employees.stream ()
                .map (EmployeeMapper::toDTO)
                .collect (Collectors.toList ());
        return ResponseEntity.ok (employeeDTOs);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee (@PathVariable String employeeId, @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity (employeeDTO);
        Employee updatedEmployee = employeeService.updateEmployee (employeeId, employee);
        return ResponseEntity.ok (EmployeeMapper.toDTO (updatedEmployee));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee (@PathVariable String employeeId) {
        employeeService.deleteEmployee (employeeId);
        return ResponseEntity.noContent ().build ();
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees (@ModelAttribute UserEmployeeSearchDTO searchDTO) {
        List<EmployeeDTO> employeeDTOs = employeeService.searchEmployees (searchDTO);
        return ResponseEntity.ok (employeeDTOs);
    }
}
