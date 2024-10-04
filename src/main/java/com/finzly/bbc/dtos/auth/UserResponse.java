package com.finzly.bbc.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
