package com.finzly.bbc.services.auth;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;
//
//    // Create a new customer from a CustomerDTO
//    public CustomerDTO createCustomer (CustomerDTO customerDTO) {
//        Customer customer = CustomerMapper.toEntity (customerDTO);
//        // Generate and set customerId using the prePersist method
//        Customer createdCustomer = customerRepository.save (customer);
//        return CustomerMapper.toDTO (createdCustomer);
//    }
//
//    // Create a customer with user details from a UserCustomerCreationDTO
//    public CustomerDTO createCustomerWithUserDetails (UserCustomerCreationDTO userCustomerCreationDTO) {
//        User user = UserCustomerCreationMapper.toUser (userCustomerCreationDTO);
//        User createdUser = userService.createUser (user);
//
//        // Create the customer with the newly created user
//        Customer customer = UserCustomerCreationMapper.toCustomer (userCustomerCreationDTO, createdUser);
//        Customer createdCustomer = customerRepository.save (customer);
//        return CustomerMapper.toDTO (createdCustomer);
//    }
//
//    // Retrieve a customer by their ID
//    public CustomerDTO getCustomerById (String customerId) {
//        Customer customer = customerRepository.findById (customerId)
//                .orElseThrow (() -> new RuntimeException ("Customer not found"));
//        return CustomerMapper.toDTO (customer);
//    }
//
//    // Retrieve all customers
//    public List<CustomerDTO> getAllCustomers () {
//        List<Customer> customers = customerRepository.findAll ();
//        return customers.stream ()
//                .map (CustomerMapper::toDTO)
//                .collect (Collectors.toList ());
//    }
//
//    // Update an existing customer by their ID
//    public CustomerDTO updateCustomer (String customerId, CustomerDTO customerDTO) {
//        // Retrieve the existing customer
//        Customer existingCustomer = customerRepository.findById (customerId)
//                .orElseThrow (() -> new RuntimeException ("Customer not found"));
//
//        // Update fields from the DTO
//        existingCustomer.setAddress (customerDTO.getAddress ());
//        // Add other fields as necessary
//
//        // Save the updated customer
//        Customer updatedCustomer = customerRepository.save (existingCustomer);
//        return CustomerMapper.toDTO (updatedCustomer);
//    }
//
//    // Delete a customer by their ID
//    public void deleteCustomer (String customerId) {
//        // Delete the customer
//        Customer customer = customerRepository.findById (customerId)
//                .orElseThrow (() -> new RuntimeException ("Customer not found"));
//        customerRepository.delete (customer);
//    }
//
//    // Search for customers based on provided parameters
//    public List<CustomerDTO> searchCustomers (String userId, String email, String phoneNumber, Boolean isAdmin) {
//        List<Customer> customers = customerRepository.searchCustomers (userId, email, phoneNumber, isAdmin);
//        return customers.stream ()
//                .map (CustomerMapper::toDTO)
//                .collect (Collectors.toList ());
//    }
}
