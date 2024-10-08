package com.finzly.bbc.repositories.auth;

import com.finzly.bbc.models.auth.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EmployeeRepository extends JpaRepository<Employee, String> {
    @Query("SELECT e FROM Employee e JOIN e.user u WHERE " +
            "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
            "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%)")
    Page<Employee> searchEmployeeWithUserDetails (
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            Pageable pageable
    );
}
