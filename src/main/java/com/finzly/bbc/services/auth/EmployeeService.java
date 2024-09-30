package com.finzly.bbc.services.auth;

import com.finzly.bbc.dto.auth.EmployeeDTO;
import com.finzly.bbc.dto.auth.UserEmployeeSearchDTO;
import com.finzly.bbc.dto.auth.mapper.EmployeeMapper;
import com.finzly.bbc.exceptions.custom.auth.EmployeeNotFoundException;
import com.finzly.bbc.models.auth.Employee;
import com.finzly.bbc.repositories.auth.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee createEmployee (Employee employee) {
        return employeeRepository.save (employee);
    }

    public Employee getEmployeeById (String employeeId) {
        return employeeRepository.findById (employeeId)
                .orElseThrow (() -> new EmployeeNotFoundException ("Employee not found with ID: " + employeeId));
    }

    public List<Employee> getAllEmployees () {
        return employeeRepository.findAll ();
    }

    public Employee updateEmployee (String employeeId, Employee employeeDetails) {
        Employee existingEmployee = getEmployeeById (employeeId);
        existingEmployee.setDesignation (employeeDetails.getDesignation ());
        existingEmployee.setSalary (employeeDetails.getSalary ());
        return employeeRepository.save (existingEmployee);
    }

    public void deleteEmployee (String employeeId) {
        Employee employee = getEmployeeById (employeeId);
        employeeRepository.delete (employee);
    }

    public List<EmployeeDTO> searchEmployees (UserEmployeeSearchDTO searchDTO) {
        return employeeRepository.findAll ().stream ()
                .filter (employee -> (searchDTO.getDesignation () == null || employee.getDesignation ().equalsIgnoreCase (searchDTO.getDesignation ())) &&
                        (searchDTO.getMinSalary () == null || employee.getSalary () >= searchDTO.getMinSalary ()) &&
                        (searchDTO.getMaxSalary () == null || employee.getSalary () <= searchDTO.getMaxSalary ()) &&
                        (searchDTO.getUserId () == null || employee.getUser ().getId ().equals (searchDTO.getUserId ())) &&
                        (searchDTO.getEmail () == null || employee.getUser ().getEmail ().equals (searchDTO.getEmail ())) &&
                        (searchDTO.getPhoneNumber () == null || employee.getUser ().getPhoneNumber ().equals (searchDTO.getPhoneNumber ())) &&
                        (searchDTO.getIsAdmin () == null || employee.getUser ().isAdmin () == searchDTO.getIsAdmin ()))
                .map (EmployeeMapper::toDTO)
                .collect (Collectors.toList ());
    }
}
