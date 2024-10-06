package com.finzly.bbc.services.auth;

import com.finzly.bbc.dtos.auth.*;
import com.finzly.bbc.dtos.common.PaginationRequest;
import com.finzly.bbc.dtos.common.PaginationResponse;
import com.finzly.bbc.exceptions.BadRequestException;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.auth.Employee;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.repositories.auth.CustomerRepository;
import com.finzly.bbc.repositories.auth.UserRepository;
import com.finzly.bbc.utils.CsvParserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    // Method to add bulk customers with user details
    public BulkUserCustomerResponse addBulkCustomersWithUserDetails(BulkUserCustomerRequest bulkUserCustomerRequest) {
        List<UserCustomerRequest> userCustomerRequests = bulkUserCustomerRequest.getUserCustomers();
        List<UserCustomerResponse> successfulResponses = new ArrayList<>();
        List<FailedUserCustomerRequest> failedRequests = new ArrayList<>(); // List for failed requests

        for (UserCustomerRequest userCustomerRequest : userCustomerRequests) {
            try {
                // Validate request
                validateUserCustomerRequest(userCustomerRequest);

                // Check if user already exists
                if (userExists(userCustomerRequest.getEmail())) {
                    log.warn("User with email {} already exists. Skipping this record.", userCustomerRequest.getEmail());
                    continue; // Skip if user exists
                }

                // Proceed to add customer
                UserCustomerResponse response = addCustomerWithUserDetails(userCustomerRequest);
                successfulResponses.add(response);

            } catch (Exception e) {
                log.error("Failed to add customer for request {}: {}", userCustomerRequest, e.getMessage());
                failedRequests.add(new FailedUserCustomerRequest(userCustomerRequest, e.getMessage())); // Add to failed requests
            }
        }

        return BulkUserCustomerResponse.builder() // Return both successful and failed responses
                .successfulResponses(successfulResponses)
                .failedRequests(failedRequests)
                .build();
    }

    // Method to check if a user already exists based on their email
    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Method to process CSV file and create bulk customers with user details
    public BulkUserCustomerResponse addBulkCustomersWithCsv(MultipartFile csvFile) {
        try {
            // Parse CSV file into list of UserCustomerRequest
            List<UserCustomerRequest> userCustomerRequests = CsvParserUtil.parseCsvFile(
                    csvFile, this::mapCsvRecordToUserCustomerRequest);

            // Call existing bulk creation method
            BulkUserCustomerRequest bulkRequest = new BulkUserCustomerRequest();
            bulkRequest.setUserCustomers(userCustomerRequests);

            return addBulkCustomersWithUserDetails(bulkRequest);

        } catch (IOException e) {
            log.error("Error processing CSV file: {}", e.getMessage());
            throw new BadRequestException("Failed to process the CSV file.");
        }
    }

    // Helper method to map a CSVRecord to UserCustomerRequest
    private UserCustomerRequest mapCsvRecordToUserCustomerRequest(CSVRecord record) {
        return UserCustomerRequest.builder()
                .firstName(record.get("firstName"))
                .lastName(record.get("lastName"))
                .email(record.get("email"))
                .phoneNumber(record.get("phoneNumber"))
                .address(record.get("address"))
                .build();
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
