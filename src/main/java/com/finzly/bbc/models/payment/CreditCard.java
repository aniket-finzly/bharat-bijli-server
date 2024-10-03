package com.finzly.bbc.models.payment;

import com.finzly.bbc.models.notification.OTP;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String number;
    private String pin;
    private Long creditLimit;
    private String cvv;

    private LocalDate expiryTime;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "otp_id", referencedColumnName = "id")
    private OTP otp;

    // One Credit Card belongs to one Account
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}



