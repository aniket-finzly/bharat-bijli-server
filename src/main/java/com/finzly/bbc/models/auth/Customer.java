package com.finzly.bbc.models.auth;

import com.finzly.bbc.models.billing.Connection;
import com.finzly.bbc.models.notification.OTP;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;

@Entity
@Table(name = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(nullable = false, unique = true)
    private String customerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "otp_id", referencedColumnName = "id")
    private OTP otp;

    // List of connections associated with this customer
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Connection> connections;

    public static String generateCustomerId () {
        return "CST" + String.format ("%07d", new Random ().nextInt (10000000));
    }

    @PrePersist
    public void onCreate () {
        this.customerId = generateCustomerId ();
    }
}
