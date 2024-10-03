package com.finzly.bbc.repositories.payment;

import com.finzly.bbc.models.payment.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByAccountNo (String accountNo);
}

