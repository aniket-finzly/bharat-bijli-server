package com.finzly.bbc.services.auth;

import com.finzly.bbc.exceptions.custom.auth.CustomerNotFoundException;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.repositories.auth.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer (Customer customer) {
        return customerRepository.save (customer);
    }

    public Customer getCustomerById (String customerId) {
        return customerRepository.findById (customerId)
                .orElseThrow (() -> new CustomerNotFoundException ("Customer not found with ID: " + customerId));
    }

    public List<Customer> getAllCustomers () {
        return customerRepository.findAll ();
    }

    public Customer updateCustomer (String customerId, Customer customerDetails) {
        Customer existingCustomer = getCustomerById (customerId);
        existingCustomer.setAddress (customerDetails.getAddress ());
        return customerRepository.save (existingCustomer);
    }

    public void deleteCustomer (String customerId) {
        Customer customer = getCustomerById (customerId);
        customerRepository.delete (customer);
    }

    public List<Customer> searchCustomers (String userId, String email, String phoneNumber, Boolean isAdmin) {
        return customerRepository.findAll ().stream ()
                .filter (customer -> (userId == null || customer.getUser ().getId ().equals (userId)) &&
                        (email == null || customer.getUser ().getEmail ().equals (email)) &&
                        (phoneNumber == null || customer.getUser ().getPhoneNumber ().equals (phoneNumber)) &&
                        (isAdmin == null || customer.getUser ().isAdmin () == isAdmin))
                .collect (Collectors.toList ());
    }
}
