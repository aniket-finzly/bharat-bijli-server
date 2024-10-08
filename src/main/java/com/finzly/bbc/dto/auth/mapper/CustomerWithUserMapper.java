package com.finzly.bbc.dto.auth.mapper;

import com.finzly.bbc.dto.auth.CustomerWithUserDTO;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.auth.User;

public class CustomerWithUserMapper {

    // Convert User and Customer entities to CustomerUserDTO
    public static CustomerWithUserDTO toDTO (User user, Customer customer) {
        return CustomerWithUserDTO.builder ()
                // User fields
                .userId (user.getId ())
                .email (user.getEmail ())
                .firstName (user.getFirstName ())
                .lastName (user.getLastName ())
                .phoneNumber (user.getPhoneNumber ())
                .isAdmin (user.isAdmin ())
                .createdAt (user.getCreatedAt ())
                .updatedAt (user.getUpdatedAt ())

                // Customer fields
                .customerId (customer.getCustomerId ())
                .address (customer.getAddress ())
                .build ();
    }

    // Convert DTO to User entity
    public static User toUser (CustomerWithUserDTO dto) {
        return User.builder ()
                .id (dto.getUserId ())  // for updates
                .email (dto.getEmail ())
                .firstName (dto.getFirstName ())
                .lastName (dto.getLastName ())
                .phoneNumber (dto.getPhoneNumber ())
                .isAdmin (dto.isAdmin ())
                .build ();
    }

    // Convert DTO to Customer entity
    public static Customer toCustomer (CustomerWithUserDTO dto, User user) {
        return Customer.builder ()
                .customerId (dto.getCustomerId ())  // for updates
                .user (user)
                .address (dto.getAddress ())
                .build ();
    }
}
