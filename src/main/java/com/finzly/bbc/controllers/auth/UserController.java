package com.finzly.bbc.controllers.auth;


import com.finzly.bbc.dto.auth.UserDTO;
import com.finzly.bbc.dto.auth.mapper.UserMapper;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.services.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser (@RequestBody UserDTO userDTO) {
        User user = UserMapper.toEntity (userDTO);
        User createdUser = userService.createUser (user);
        return ResponseEntity.ok (UserMapper.toDTO (createdUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById (@PathVariable String id) {
        User user = userService.getUserById (id);
        return ResponseEntity.ok (UserMapper.toDTO (user));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers () {
        List<User> users = userService.getAllUsers ();
        List<UserDTO> userDTOs = users.stream ()
                .map (UserMapper::toDTO)
                .collect (Collectors.toList ());
        return ResponseEntity.ok (userDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser (@PathVariable String id, @RequestBody UserDTO userDTO) {
        User user = UserMapper.toEntity (userDTO);
        User updatedUser = userService.updateUser (id, user);
        return ResponseEntity.ok (UserMapper.toDTO (updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable String id) {
        userService.deleteUser (id);
        return ResponseEntity.noContent ().build ();
    }
}


