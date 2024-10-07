package com.finzly.bbc.repositories.payment;

import com.finzly.bbc.models.payment.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("SELECT a FROM Account a WHERE a.accountNo = :accountNo")
    Optional<Account> findByAccountNo (@Param("accountNo") String accountNo);
}

