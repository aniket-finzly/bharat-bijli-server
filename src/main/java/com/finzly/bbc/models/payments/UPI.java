package com.finzly.bbc.models.payments;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// UPI Class
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class UPI extends PaymentDetail {
    @NotBlank
    private String upiId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // Reference to the account associated with this UPI

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sensitive_data_id", referencedColumnName = "id")
    private UPISensitiveData sensitiveData; // Reference to sensitive data
}
