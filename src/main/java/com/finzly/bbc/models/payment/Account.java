package com.finzly.bbc.models.payment;

import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.utils.RandomUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    private String accountNo;

    private String bankName;
    private String ifsc;
    private Double balance;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Upi> upiIds;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private DebitCard debitCard;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "senderAccountId")
    private List<Transaction> transactions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public String generateAccountNo () {
        return RandomUtil.generateRandomNumericString (10);
    }
}
