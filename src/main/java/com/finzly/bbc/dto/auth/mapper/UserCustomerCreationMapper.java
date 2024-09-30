package com.finzly.bbc.dto.auth.mapper;

import com.finzly.bbc.dto.auth.UserCustomerCreationDTO;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.auth.User;

public class UserCustomerCreationMapper {

    public static User toUser (UserCustomerCreationDTO dto) {
        return User.builder ()
                .email (dto.getEmail ())
                .firstName (dto.getFirstName ())
                .lastName (dto.getLastName ())
                .phoneNumber (dto.getPhoneNumber ())
                .isAdmin (dto.isAdmin ())
                .build ();
    }

    public static Customer toCustomer (UserCustomerCreationDTO dto, User user) {
        return Customer.builder ()
                .user (user)
                .address (dto.getAddress ())
                .build ();
    }
}
