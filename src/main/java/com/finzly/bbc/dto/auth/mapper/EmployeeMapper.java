package com.finzly.bbc.dto.auth.mapper;

import com.finzly.bbc.dto.auth.EmployeeDTO;
import com.finzly.bbc.models.auth.Employee;

public class EmployeeMapper {

    public static EmployeeDTO toDTO (Employee employee) {
        return EmployeeDTO.builder ()
                .employeeId (employee.getEmployeeId ())
                .designation (employee.getDesignation ())
                .salary (employee.getSalary ())
                .userId (employee.getUser ().getId ())
                .build ();
    }

    public static Employee toEntity (EmployeeDTO employeeDTO) {
        return Employee.builder ()
                .employeeId (employeeDTO.getEmployeeId ())
                .designation (employeeDTO.getDesignation ())
                .salary (employeeDTO.getSalary ())
                .build ();
    }
}

