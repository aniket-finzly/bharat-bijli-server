package com.finzly.bbc.repositories.auth;

import com.finzly.bbc.models.auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE " +
            "(:userId IS NULL OR u.id = :userId) AND " +
            "(:email IS NULL OR u.email = :email) AND " +
            "(:phoneNumber IS NULL OR u.phoneNumber = :phoneNumber) AND " +
            "(:isAdmin IS NULL OR u.isAdmin = :isAdmin) AND " +
            "(:firstName IS NULL OR u.firstName LIKE CONCAT('%', :firstName, '%')) AND " +
            "(:lastName IS NULL OR u.lastName LIKE CONCAT('%', :lastName, '%'))")
    Page<User> searchUsers (@Param("userId") String userId,
                            @Param("email") String email,
                            @Param("phoneNumber") String phoneNumber,
                            @Param("isAdmin") Boolean isAdmin,
                            @Param("firstName") String firstName,
                            @Param("lastName") String lastName,
                            Pageable pageable);
}
