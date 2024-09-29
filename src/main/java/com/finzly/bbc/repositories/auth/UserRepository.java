package com.finzly.bbc.repositories.auth;

import com.finzly.bbc.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmployee_EmployeeId (String employeeId);

    Optional<User> findByCustomer_CustomerId (String customerId);
}
