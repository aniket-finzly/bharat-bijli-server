package com.finzly.bbc.dto.auth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerWithUserDTO {
    // User fields
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isAdmin;

    // Timestamps for audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Customer fields
    private String customerId;
    private String address;
    private String otpId;
}



