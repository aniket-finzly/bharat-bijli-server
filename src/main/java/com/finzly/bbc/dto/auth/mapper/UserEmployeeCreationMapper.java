package com.finzly.bbc.dto.auth.mapper;

import com.finzly.bbc.dto.auth.UserEmployeeCreationDTO;
import com.finzly.bbc.models.auth.Employee;
import com.finzly.bbc.models.auth.User;

public class UserEmployeeCreationMapper {

    public static User toUser (UserEmployeeCreationDTO dto) {
        return User.builder ()
                .email (dto.getEmail ())
                .firstName (dto.getFirstName ())
                .lastName (dto.getLastName ())
                .phoneNumber (dto.getPhoneNumber ())
                .isAdmin (dto.isAdmin ())
                .build ();
    }

    public static Employee toEmployee (UserEmployeeCreationDTO dto, User user) {
        return Employee.builder ()
                .user (user)
                .designation (dto.getDesignation ())
                .salary (dto.getSalary ())
                .build ();
    }
}




