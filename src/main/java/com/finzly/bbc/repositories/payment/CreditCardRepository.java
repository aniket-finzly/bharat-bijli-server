package com.finzly.bbc.repositories.payment;

import com.finzly.bbc.models.payment.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    Optional<CreditCard> findByNumber (String number);
}

