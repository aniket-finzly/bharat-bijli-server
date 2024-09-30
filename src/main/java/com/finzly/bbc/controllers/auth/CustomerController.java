package com.finzly.bbc.controllers.auth;

import com.finzly.bbc.dto.auth.CustomerDTO;
import com.finzly.bbc.dto.auth.UserCustomerCreationDTO;
import com.finzly.bbc.dto.auth.mapper.CustomerMapper;
import com.finzly.bbc.dto.auth.mapper.UserCustomerCreationMapper;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.services.auth.CustomerService;
import com.finzly.bbc.services.auth.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/customers")
@Tag(name = "Customer", description = "Customer API for creating, updating, deleting and getting customers with user details")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer (@RequestBody CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.toEntity (customerDTO);
        Customer createdCustomer = customerService.createCustomer (customer);
        return ResponseEntity.ok (CustomerMapper.toDTO (createdCustomer));
    }

    @PostMapping("/create-with-user")
    public ResponseEntity<CustomerDTO> createCustomerWithUserDetails (@RequestBody UserCustomerCreationDTO userCustomerCreationDTO) {
        User user = UserCustomerCreationMapper.toUser (userCustomerCreationDTO);
        User createdUser = userService.createUser (user);
        Customer customer = UserCustomerCreationMapper.toCustomer (userCustomerCreationDTO, createdUser);
        Customer createdCustomer = customerService.createCustomer (customer);
        return ResponseEntity.ok (CustomerMapper.toDTO (createdCustomer));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById (@PathVariable String customerId) {
        Customer customer = customerService.getCustomerById (customerId);
        return ResponseEntity.ok (CustomerMapper.toDTO (customer));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers () {
        List<Customer> customers = customerService.getAllCustomers ();
        List<CustomerDTO> customerDTOs = customers.stream ()
                .map (CustomerMapper::toDTO)
                .collect (Collectors.toList ());
        return ResponseEntity.ok (customerDTOs);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer (@PathVariable String customerId, @RequestBody CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.toEntity (customerDTO);
        Customer updatedCustomer = customerService.updateCustomer (customerId, customer);
        return ResponseEntity.ok (CustomerMapper.toDTO (updatedCustomer));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer (@PathVariable String customerId) {
        customerService.deleteCustomer (customerId);
        return ResponseEntity.noContent ().build ();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> searchCustomers (
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Boolean isAdmin) {

        List<Customer> customers = customerService.searchCustomers (userId, email, phoneNumber, isAdmin);
        List<CustomerDTO> customerDTOs = customers.stream ()
                .map (CustomerMapper::toDTO)
                .collect (Collectors.toList ());
        return ResponseEntity.ok (customerDTOs);
    }
}
