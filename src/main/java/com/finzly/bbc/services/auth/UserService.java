package com.finzly.bbc.services.auth;

import com.finzly.bbc.exceptions.custom.auth.UserNotFoundException;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.repositories.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser (User user) {
        return userRepository.save (user);
    }

    public User getUserById (String id) {
        return userRepository.findById (id)
                .orElseThrow (() -> new UserNotFoundException ("User not found with ID: " + id));
    }

    public List<User> getAllUsers () {
        return userRepository.findAll ();
    }

    public User updateUser (String id, User userDetails) {
        User existingUser = getUserById (id);
        existingUser.setEmail (userDetails.getEmail ());
        existingUser.setFirstName (userDetails.getFirstName ());
        existingUser.setLastName (userDetails.getLastName ());
        existingUser.setPhoneNumber (userDetails.getPhoneNumber ());
        existingUser.setAdmin (userDetails.isAdmin ());
        return userRepository.save (existingUser);
    }

    public void deleteUser (String id) {
        User user = getUserById (id);
        userRepository.delete (user);
    }
}
