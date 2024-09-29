package com.finzly.bbc.models.notification;

import com.finzly.bbc.utils.OtpUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "otps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "OTP is mandatory")
    @Column(length = 6, unique = true, nullable = false)
    private String otp;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private int otpAttemptCount = 0;

    public static final int MAX_OTP_ATTEMPTS = 3;


    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isAttemptLimitExceeded() {
        return otpAttemptCount >= MAX_OTP_ATTEMPTS;
    }

    public boolean isValidOtp() {
        return !isExpired() && !isAttemptLimitExceeded();
    }

    @PrePersist
    public void generateOtp() {
        this.otp = OtpUtil.generateOtpCode(6);
        this.otpAttemptCount = 0;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.plusMinutes(5);
    }


}
