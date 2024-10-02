package com.finzly.bbc.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCustomerCreationDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
}

