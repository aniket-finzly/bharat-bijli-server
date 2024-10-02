package com.finzly.bbc.controllers.auth;

import com.finzly.bbc.dto.auth.CustomerDTO;
import com.finzly.bbc.dto.auth.UserCustomerCreationDTO;
import com.finzly.bbc.services.auth.CustomerService;
import com.finzly.bbc.services.auth.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.ok (customerService.createCustomer (customerDTO));
    }

    @PostMapping("/create-with-user")
    public ResponseEntity<CustomerDTO> createCustomerWithUserDetails (@RequestBody UserCustomerCreationDTO userCustomerCreationDTO) {
        return ResponseEntity.ok (customerService.createCustomerWithUserDetails (userCustomerCreationDTO));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById (@PathVariable String customerId) {
        return ResponseEntity.ok (customerService.getCustomerById (customerId));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers () {
        return ResponseEntity.ok (customerService.getAllCustomers ());
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer (@PathVariable String customerId, @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok (customerService.updateCustomer (customerId, customerDTO));
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

        return ResponseEntity.ok (customerService.searchCustomers (userId, email, phoneNumber, isAdmin));
    }
}
