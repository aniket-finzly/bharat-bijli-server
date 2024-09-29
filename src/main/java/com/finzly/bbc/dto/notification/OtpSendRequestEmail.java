package com.finzly.bbc.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpSendRequestEmail {
    @NotBlank
    private String email;
}
