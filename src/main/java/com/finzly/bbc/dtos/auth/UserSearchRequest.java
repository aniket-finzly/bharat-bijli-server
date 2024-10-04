package com.finzly.bbc.dtos.auth;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearchRequest {
    private String id;
    @Email(message = "Email should be valid")
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean isAdmin;
}
