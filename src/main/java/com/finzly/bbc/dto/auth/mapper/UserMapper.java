package com.finzly.bbc.dto.auth.mapper;

import com.finzly.bbc.dto.auth.UserDTO;
import com.finzly.bbc.models.auth.User;

public class UserMapper {

    public static UserDTO toDTO (User user) {
        return UserDTO.builder ()
                .id (user.getId ())
                .email (user.getEmail ())
                .firstName (user.getFirstName ())
                .lastName (user.getLastName ())
                .phoneNumber (user.getPhoneNumber ())
                .isAdmin (user.isAdmin ())
                .build ();
    }

    public static User toEntity (UserDTO userDTO) {
        return User.builder ()
                .id (userDTO.getId ())
                .email (userDTO.getEmail ())
                .firstName (userDTO.getFirstName ())
                .lastName (userDTO.getLastName ())
                .phoneNumber (userDTO.getPhoneNumber ())
                .isAdmin (userDTO.isAdmin ())
                .build ();
    }
}



