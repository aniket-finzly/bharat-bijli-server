package com.finzly.bbc.controllers.auth;

import com.finzly.bbc.dtos.auth.*;
import com.finzly.bbc.dtos.common.PaginationRequest;
import com.finzly.bbc.dtos.common.PaginationResponse;
import com.finzly.bbc.response.CustomApiResponse;
import com.finzly.bbc.services.auth.CustomerService;
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
@Tag(name = "User and Customer API", description = "API for managing users and customers with search, pagination, and CRUD operations")
public class UserController {

    private final UserService userService;
    private final CustomerService customerService;

    // User endpoints
    @PostMapping("/users")
    @Operation(summary = "Create User", description = "Create a new user")
    public ResponseEntity<CustomApiResponse<UserResponse>> createUser (@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.createUser (userRequest);
        return ResponseEntity.status (HttpStatus.CREATED)
                .body (CustomApiResponse.success ("User created successfully", userResponse, HttpStatus.CREATED.value ()));
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

    @GetMapping("/users")
    @Operation(summary = "Search Users with Pagination", description = "Search users with optional filters and pagination")
    public ResponseEntity<CustomApiResponse<PaginationResponse<UserResponse>>> searchUsers (
            @ModelAttribute UserSearchRequest searchRequest,
            @ModelAttribute PaginationRequest paginationRequest) {

        if (paginationRequest.getPage () == null) paginationRequest.setPage (0);
        if (paginationRequest.getSize () == null) paginationRequest.setSize (10);

        PaginationResponse<UserResponse> response = userService.searchUsersWithPagination (searchRequest, paginationRequest);
        return ResponseEntity.ok (CustomApiResponse.success ("Users fetched successfully", response, HttpStatus.OK.value ()));
    }

    // Customer endpoints
    @PostMapping("/customers")
    @Operation(summary = "Create Customer", description = "Create a new customer with user details")
    public ResponseEntity<CustomApiResponse<UserCustomerResponse>> createCustomer (
            @RequestBody @Valid UserCustomerRequest userCustomerRequest) {
        UserCustomerResponse customerResponse = customerService.addCustomerWithUserDetails (userCustomerRequest);
        return ResponseEntity.status (HttpStatus.CREATED)
                .body (CustomApiResponse.success ("Customer created successfully", customerResponse, HttpStatus.CREATED.value ()));
    }

    @GetMapping("/customers/{id}")
    @Operation(summary = "Get Customer by ID", description = "Retrieve a customer by their ID")
    public ResponseEntity<CustomApiResponse<UserCustomerResponse>> getCustomerById (@PathVariable String id) {
        UserCustomerResponse customerResponse = customerService.getCustomerById (id);
        return ResponseEntity.ok (CustomApiResponse.success ("Customer fetched successfully", customerResponse, HttpStatus.OK.value ()));
    }

    @PutMapping("/customers/{id}")
    @Operation(summary = "Update Customer", description = "Update customer details by ID")
    public ResponseEntity<CustomApiResponse<UserCustomerResponse>> updateCustomer (
            @PathVariable String id,
            @RequestBody @Valid UserCustomerRequest userCustomerRequest) {
        UserCustomerResponse customerResponse = customerService.updateUserCustomer (id, userCustomerRequest);
        return ResponseEntity.ok (CustomApiResponse.success ("Customer updated successfully", customerResponse, HttpStatus.OK.value ()));
    }

    @DeleteMapping("/customers/{id}")
    @Operation(summary = "Delete Customer", description = "Delete a customer by their ID")
    public ResponseEntity<CustomApiResponse<String>> deleteCustomer (@PathVariable String id) {
        customerService.deleteCustomer (id);
        return ResponseEntity.ok (CustomApiResponse.success ("Customer deleted successfully", null, HttpStatus.OK.value ()));
    }

    @GetMapping("/customers")
    @Operation(summary = "Search Customers with Pagination", description = "Search customers with optional filters and pagination")
    public ResponseEntity<CustomApiResponse<PaginationResponse<UserCustomerResponse>>> searchCustomers (
            @ModelAttribute PaginationRequest paginationRequest,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email
    ) {
        // Set default pagination values if not provided
        if (paginationRequest.getPage () == null) paginationRequest.setPage (0);
        if (paginationRequest.getSize () == null) paginationRequest.setSize (10);

        PaginationResponse<UserCustomerResponse> response = customerService.searchCustomersWithPagination (
                paginationRequest, firstName, lastName, email
        );

        return ResponseEntity.ok (CustomApiResponse.success ("Customers fetched successfully", response, HttpStatus.OK.value ()));
    }

}
