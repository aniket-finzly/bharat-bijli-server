package com.finzly.bbc.services.auth;

import com.finzly.bbc.dtos.auth.*;
import com.finzly.bbc.dtos.common.PaginationRequest;
import com.finzly.bbc.dtos.common.PaginationResponse;
import com.finzly.bbc.exceptions.BadRequestException;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.repositories.auth.CustomerRepository;
import com.finzly.bbc.repositories.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    private static User getUser (UserCustomerRequest userCustomerRequest, Customer customer) {
        User user = customer.getUser ();
        if (userCustomerRequest.getEmail () != null) {
            user.setEmail (userCustomerRequest.getEmail ());
        }
        if (userCustomerRequest.getPhoneNumber () != null) {
            user.setPhoneNumber (userCustomerRequest.getPhoneNumber ());
        }
        if (userCustomerRequest.getFirstName () != null) {
            user.setFirstName (userCustomerRequest.getFirstName ());
        }
        if (userCustomerRequest.getLastName () != null) {
            user.setLastName (userCustomerRequest.getLastName ());
        }
        return user;
    }

    // Create a new customer
    public UserCustomerResponse createCustomer (CustomerRequest customerRequest) {
        if (customerRequest.getUserId () == null || customerRequest.getUserId ().isEmpty ()) {
            throw new BadRequestException ("User ID is mandatory.");
        }

        User user = userRepository.findById (customerRequest.getUserId ())
                .orElseThrow (() -> new ResourceNotFoundException ("User not found with ID: " + customerRequest.getUserId ()));

        Customer customer = modelMapper.map (customerRequest, Customer.class);
        customer.setUser (user);

        Customer savedCustomer = customerRepository.save (customer);

        return mapToUserCustomerResponse (savedCustomer);
    }

    // Add customer with user details
    public UserCustomerResponse addCustomerWithUserDetails (UserCustomerRequest userCustomerRequest) {
        validateUserCustomerRequest (userCustomerRequest);

        UserRequest userRequest = modelMapper.map (userCustomerRequest, UserRequest.class);
        // Create user to get user ID
        UserResponse userResponse = userService.createUser (userRequest);

        String userId = userResponse.getId ();

        CustomerRequest customerRequest = new CustomerRequest ();
        customerRequest.setUserId (userId);
        customerRequest.setAddress (userCustomerRequest.getAddress ());

        // Directly return the response from createCustomer
        return createCustomer (customerRequest);
    }

    // Get customer by ID
    public UserCustomerResponse getCustomerById (String customerId) {
        Customer customer = customerRepository.findById (customerId)
                .orElseThrow (() -> new ResourceNotFoundException ("Customer not found with ID: " + customerId));

        return mapToUserCustomerResponse (customer);
    }

    // Update user and customer details
    public UserCustomerResponse updateUserCustomer (String customerId, UserCustomerRequest userCustomerRequest) {
        // Find the customer by ID
        Customer customer = customerRepository.findById (customerId)
                .orElseThrow (() -> new ResourceNotFoundException ("Customer not found with ID: " + customerId));

        // Update user details
        var user = getUser (userCustomerRequest, customer);
        userRepository.save (user); // Save updated user

        // Update customer details
        if (userCustomerRequest.getAddress () != null) {
            customer.setAddress (userCustomerRequest.getAddress ());
        }
        Customer updatedCustomer = customerRepository.save (customer); // Save updated customer

        return mapToUserCustomerResponse (updatedCustomer);
    }

    // Delete customer
    public void deleteCustomer (String customerId) {
        Customer customer = customerRepository.findById (customerId)
                .orElseThrow (() -> new ResourceNotFoundException ("Customer not found with ID: " + customerId));

        if (customer.getUser ().getEmployee () == null) {
            userRepository.delete (customer.getUser ());
            customerRepository.delete (customer);
        } else {
            User user = customer.getUser ();
            user.setCustomer (null);
            userRepository.save (user);
            customerRepository.delete (customer);
        }
    }

    // Search customers with user details and pagination
    public PaginationResponse<UserCustomerResponse> searchCustomersWithPagination (
            PaginationRequest paginationRequest,
            String firstName,
            String lastName,
            String email
    ) {
        Pageable pageable = PageRequest.of (
                paginationRequest.getPage (),
                paginationRequest.getSize ()
        );

        Page<Customer> customerPage = customerRepository.searchCustomersWithUserDetails (firstName, lastName, email, pageable);

        List<UserCustomerResponse> customerResponses = customerPage.getContent ().stream ()
                .map (this::mapToUserCustomerResponse)
                .toList ();

        return PaginationResponse.<UserCustomerResponse>builder ()
                .content (customerResponses)
                .totalPages (customerPage.getTotalPages ())
                .totalElements (customerPage.getTotalElements ())
                .size (customerPage.getSize ())
                .number (customerPage.getNumber ())
                .build ();
    }


    // Helper method to map Customer to UserCustomerResponse
    private UserCustomerResponse mapToUserCustomerResponse (Customer customer) {
        User user = customer.getUser ();
        return UserCustomerResponse.builder ()
                .userId (user.getId ())
                .customerId (customer.getCustomerId ())
                .firstName (user.getFirstName ())
                .lastName (user.getLastName ())
                .email (user.getEmail ())
                .phoneNumber (user.getPhoneNumber ())
                .address (customer.getAddress ())
                .build ();
    }

    // Helper method to validate user customer request
    private void validateUserCustomerRequest (UserCustomerRequest userCustomerRequest) {
        if (userCustomerRequest.getPhoneNumber () == null || userCustomerRequest.getPhoneNumber ().isEmpty ()) {
            throw new BadRequestException ("Phone number is mandatory.");
        }

        if (userCustomerRequest.getEmail () == null || userCustomerRequest.getEmail ().isEmpty ()) {
            throw new BadRequestException ("Email is mandatory.");
        }

        if (userCustomerRequest.getFirstName () == null || userCustomerRequest.getFirstName ().isEmpty ()) {
            throw new BadRequestException ("First name is mandatory.");
        }

        if (userCustomerRequest.getLastName () == null || userCustomerRequest.getLastName ().isEmpty ()) {
            throw new BadRequestException ("Last name is mandatory.");
        }
    }
}
