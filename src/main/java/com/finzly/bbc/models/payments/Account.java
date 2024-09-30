package com.finzly.bbc.models.payments;

import com.finzly.bbc.models.auth.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


// Account Class
@Entity
@Data
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    @NotBlank
    private String number;

    @NotBlank
    private String name;

    @NotBlank
    private String bank;

    @NotBlank
    private String ifsc;

    @NotBlank
    private String branch;

    @NotNull
    @Positive
    private BigDecimal balance = BigDecimal.ZERO;

    private LocalDateTime createdAt = LocalDateTime.now ();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Reference to the user associated with this account
}
