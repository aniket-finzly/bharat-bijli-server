package com.finzly.bbc.controllers.auth;

import com.finzly.bbc.dtos.auth.UserRequest;
import com.finzly.bbc.dtos.auth.UserResponse;
import com.finzly.bbc.dtos.auth.UserSearchRequest;
import com.finzly.bbc.dtos.common.PaginationRequest;
import com.finzly.bbc.dtos.common.PaginationResponse;
import com.finzly.bbc.response.CustomApiResponse;
import com.finzly.bbc.services.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "User API", description = "API for managing users with search, pagination, and CRUD operations")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @Operation(summary = "Create User", description = "Create a new user")
    public ResponseEntity<CustomApiResponse<UserResponse>> createUser (@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.createUser (userRequest);
        return ResponseEntity.status (HttpStatus.CREATED).body (CustomApiResponse.success ("User created successfully", userResponse, HttpStatus.CREATED.value ()));
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get User by ID", description = "Retrieve a user by their ID")
    public ResponseEntity<CustomApiResponse<UserResponse>> getUserById (@PathVariable String id) {
        UserResponse userResponse = userService.getUserById (id);
        return ResponseEntity.ok (CustomApiResponse.success ("User fetched successfully", userResponse, HttpStatus.OK.value ()));
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Update User", description = "Update user details by ID")
    public ResponseEntity<CustomApiResponse<UserResponse>> updateUser (
            @PathVariable String id,
            @RequestBody @Valid UserRequest userRequest) {

        UserResponse userResponse = userService.updateUser (id, userRequest);
        return ResponseEntity.ok (CustomApiResponse.success ("User updated successfully", userResponse, HttpStatus.OK.value ()));
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete User", description = "Delete a user by their ID")
    public ResponseEntity<CustomApiResponse<String>> deleteUser (@PathVariable String id) {
        userService.deleteUser (id);
        return ResponseEntity.ok (CustomApiResponse.success ("User deleted successfully", null, HttpStatus.OK.value ()));
    }

    @GetMapping("/users/search")
    @Operation(summary = "Search Users with Pagination", description = "Search users with optional filters and pagination")
    public ResponseEntity<CustomApiResponse<PaginationResponse<UserResponse>>> searchUsers (
            @ModelAttribute UserSearchRequest searchRequest,  // Use @ModelAttribute to bind query params
            @ModelAttribute PaginationRequest paginationRequest) {  // Same for paginationRequest

        // Initialize PaginationRequest with defaults if needed
        if (paginationRequest.getPage () == null) {
            paginationRequest.setPage (0);  // Default page
        }
        if (paginationRequest.getSize () == null) {
            paginationRequest.setSize (10);  // Default size
        }
        if (paginationRequest.getSortBy () == null) {
            paginationRequest.setSortBy ("createdAt");
        }
        if (paginationRequest.getSortDirection () == null) {
            paginationRequest.setSortDirection ("asc");  // Default sort direction
        }

        PaginationResponse<UserResponse> response = userService.searchUsersWithPagination (searchRequest, paginationRequest);

        return ResponseEntity.ok (CustomApiResponse.success ("Users fetched successfully", response, HttpStatus.OK.value ()));
    }

}
