package com.finzly.bbc.repositories.auth;

import com.finzly.bbc.models.auth.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}
