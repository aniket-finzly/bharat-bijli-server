package com.finzly.bbc.repositories.payments;


import com.finzly.bbc.models.payments.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


// Account Repository
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    // You can define custom query methods here, if needed
    Account findByNumber (String number);
}


