package com.finzly.bbc.dtos.auth;

import lombok.Data;

@Data
public class CsvUserCustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
}
