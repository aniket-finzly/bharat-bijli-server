package com.finzly.bbc.services.auth;

import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.repositories.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername (String userId) throws UsernameNotFoundException {
        User user = userRepository.findById (userId)
                .orElseThrow (() -> new UsernameNotFoundException ("User not found with ID: " + userId));

        return new org.springframework.security.core.userdetails.User (user.getId (), "", getAuthorities (user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities (User user) {
        // Set roles based on user type (admin, employee, customer, etc.)
        String role = user.isAdmin () ? "ROLE_ADMIN" : "ROLE_USER";
        return Collections.singletonList (new SimpleGrantedAuthority (role));
    }
}
