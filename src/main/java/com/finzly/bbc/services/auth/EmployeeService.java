package com.finzly.bbc.services.auth;

import com.finzly.bbc.dto.auth.EmployeeDTO;
import com.finzly.bbc.dto.auth.UserEmployeeSearchDTO;
import com.finzly.bbc.dto.auth.mapper.EmployeeMapper;
import com.finzly.bbc.dtos.auth.*;
import com.finzly.bbc.exceptions.BadRequestException;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.exceptions.custom.auth.EmployeeNotFoundException;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.auth.Employee;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.repositories.auth.EmployeeRepository;
import com.finzly.bbc.repositories.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;



    private static User getUser (UserCustomerRequest userCustomerRequest, Customer customer) {
        User user = customer.getUser ();
        if (userCustomerRequest.getEmail () != null) {
            user.setEmail (userCustomerRequest.getEmail ());
        }
        if (userCustomerRequest.getPhoneNumber () != null) {
            user.setPhoneNumber (userCustomerRequest.getPhoneNumber ());
        }
        if (userCustomerRequest.getFirstName () != null) {
            user.setFirstName (userCustomerRequest.getFirstName ());
        }
        if (userCustomerRequest.getLastName () != null) {
            user.setLastName (userCustomerRequest.getLastName ());
        }
        return user;
    }

    // Create a new EMployee
    public UserEmployeeResponse createEmployee (EmployeeRequest employeeRequest) {
        if (employeeRequest.getUserId () == null || employeeRequest.getUserId ().isEmpty ()) {
            throw new BadRequestException("User ID is mandatory.");
        }

        User user = userRepository.findById (employeeRequest.getUserId ())
                .orElseThrow (() -> new ResourceNotFoundException("User not found with ID: " + employeeRequest.getUserId ()));

        Employee employee = modelMapper.map (employeeRequest, Employee.class);
        employee.setUser (user);

        Employee savedEmployee = employeeRepository.save (employee);

        return mapToUserEmployeeResponse (savedEmployee);
    }

    // Add Employee with user details
    public UserEmployeeResponse addEmployeeWithUserDetails (UserEmployeeRequest userEmployeeRequest) {
        validateUserEmployeeRequest (userEmployeeRequest);

        UserRequest userRequest = modelMapper.map (userEmployeeRequest, UserRequest.class);
        // Create user to get user ID
        UserResponse userResponse = userService.createUser (userRequest);

        String userId = userResponse.getId ();

        EmployeeRequest employeeRequest = new EmployeeRequest ();
        employeeRequest.setUserId (userId);
        employeeRequest.setDesignation (userEmployeeRequest.getDesignation ());
        employeeRequest.setSalary (userEmployeeRequest.getSalary());


        // Directly return the response from createEmployee
        return createEmployee (employeeRequest);
    }


    // Get Employee by ID
    public UserEmployeeResponse getEmployeeById (String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow (() -> new ResourceNotFoundException ("Employee not found with ID: " + employeeId));

        return mapToUserEmployeeResponse(employee);
    }

    // Update user and Employee details
    public UserEmployeeResponse updateUserCustomer(String employeeId, UserEmployeeRequest userEmployeeRequest) {
        // Find the employee by ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        // Update user details
        User user = employee.getUser(); // Assuming Employee has a getUser() method
        if (userEmployeeRequest.getFirstName() != null) {
            user.setFirstName(userEmployeeRequest.getFirstName());
        }
        if (userEmployeeRequest.getLastName() != null) {
            user.setLastName(userEmployeeRequest.getLastName());
        }
        if (userEmployeeRequest.getEmail() != null) {
            user.setEmail(userEmployeeRequest.getEmail());
        }
        if (userEmployeeRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(userEmployeeRequest.getPhoneNumber());
        }

        userRepository.save(user); // Save updated user

        // Update employee details
        if (userEmployeeRequest.getDesignation() != null) {
            employee.setDesignation(userEmployeeRequest.getDesignation());
        }

        Employee updatedEmployee = employeeRepository.save(employee); // Save updated employee

        return mapToUserEmployeeResponse(updatedEmployee);
    }

    // Delete customer
    public void deleteEmployee (String employeeId) {
        Employee employee = employeeRepository.findById (employeeId)
                .orElseThrow (() -> new ResourceNotFoundException ("Customer not found with ID: " + employeeId));

        if (employee.getUser ().getEmployee () == null) {
            userRepository.delete (employee.getUser ());
            employeeRepository.delete (employee);
        } else {
            User user = employee.getUser ();
            user.setEmployee (null);
            userRepository.save (user);
            employeeRepository.delete (employee);
        }
    }

    // Helper method to map Employee to UserEmployeeResponse
    private UserEmployeeResponse mapToUserEmployeeResponse (Employee employee) {
        User user = employee.getUser ();
        return UserEmployeeResponse.builder ()
                .userId (user.getId ())
                .employeeId (employee.getEmployeeId ())
                .firstName (user.getFirstName ())
                .lastName (user.getLastName ())
                .email (user.getEmail ())
                .phoneNumber (user.getPhoneNumber ())
                .designation(employee.getDesignation())
                .salary(employee.getSalary())
                .build ();
    }

    // Helper method to validate user customer request
    private void validateUserEmployeeRequest (UserEmployeeRequest userEmployeeRequest) {
        if (userEmployeeRequest.getPhoneNumber () == null || userEmployeeRequest.getPhoneNumber ().isEmpty ()) {
            throw new BadRequestException ("Phone number is mandatory.");
        }

        if (userEmployeeRequest.getEmail () == null || userEmployeeRequest.getEmail ().isEmpty ()) {
            throw new BadRequestException ("Email is mandatory.");
        }

        if (userEmployeeRequest.getFirstName () == null || userEmployeeRequest.getFirstName ().isEmpty ()) {
            throw new BadRequestException ("First name is mandatory.");
        }

        if (userEmployeeRequest.getLastName () == null || userEmployeeRequest.getLastName ().isEmpty ()) {
            throw new BadRequestException ("Last name is mandatory.");
        }
    }




}
