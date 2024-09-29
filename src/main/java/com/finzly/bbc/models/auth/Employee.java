package com.finzly.bbc.models.auth;

import com.finzly.bbc.models.notification.OTP;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Entity
@Table(name = "employees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @Column(nullable = false, unique = true)
    private String employeeId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String designation;

    private double salary;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "otp_id", referencedColumnName = "id")
    private OTP otp;

    @PrePersist
    public void onCreate () {
        this.employeeId = generateEmployeeId ();
    }

    public static String generateEmployeeId () {
        return "EMP" + String.format ("%07d", new Random ().nextInt (10000000));
    }
}
