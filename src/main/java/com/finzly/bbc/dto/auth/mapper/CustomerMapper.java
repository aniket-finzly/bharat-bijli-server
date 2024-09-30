package com.finzly.bbc.dto.auth.mapper;

import com.finzly.bbc.dto.auth.CustomerDTO;
import com.finzly.bbc.models.auth.Customer;

public class CustomerMapper {

    public static CustomerDTO toDTO (Customer customer) {
        return CustomerDTO.builder ()
                .customerId (customer.getCustomerId ())
                .address (customer.getAddress ())
                .userId (customer.getUser ().getId ())
                .build ();
    }

    public static Customer toEntity (CustomerDTO customerDTO) {
        return Customer.builder ()
                .customerId (customerDTO.getCustomerId ())
                .address (customerDTO.getAddress ())
                .build ();
    }
}
