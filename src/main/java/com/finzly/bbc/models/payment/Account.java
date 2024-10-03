package com.finzly.bbc.models.payment;

import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.utils.RandomUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Store the encrypted account number
    private String accountNo;

    private String bankName;
    private String ifsc;
    private Long accountBalance;

    @Column(updatable = false)
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Upi> upiIds;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CreditCard creditCard;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DebitCard debitCard;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "senderAccountId")
    private List<Transaction> transactions;

    @PrePersist
    protected void onCreate () {
        this.createdTime = LocalDateTime.now ();
        this.accountNo = generateAccountNo ();
    }

    private String generateAccountNo () {
        return RandomUtil.generateRandomNumericString (10);
    }
}
