package com.finzly.bbc.controllers.payment;

import com.finzly.bbc.dtos.payment.*;
import com.finzly.bbc.models.payment.Transaction;
import com.finzly.bbc.response.CustomApiResponse;
import com.finzly.bbc.services.payment.AccountService;
import com.finzly.bbc.services.payment.UpiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment API", description = "API for creating payments, accounts, and retrieving payment details")
public class PaymentController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UpiService upiService;


    @Operation(summary = "Create a Payment", description = "Creates a new payment and returns the payment details")
    @PostMapping("/accounts")
    public ResponseEntity<CustomApiResponse<AccountResponse>> createAccount (AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.createAccount (accountRequest);
        return ResponseEntity.ok (CustomApiResponse.success ("Account created successfully", accountResponse, HttpStatus.CREATED.value ()));
    }

    @Operation(summary = "Get Account by ID", description = "Retrieves an account by its ID")
    @GetMapping("/accounts/{id}")
    public ResponseEntity<CustomApiResponse<AccountResponse>> getAccountById (@PathVariable String id) {
        AccountResponse accountResponse = accountService.getAccountById (id);
        return ResponseEntity.ok (CustomApiResponse.success ("Account fetched successfully", accountResponse, HttpStatus.OK.value ()));
    }

    @Operation(summary = "Get Account by Account Number", description = "Retrieves an account by its account number")
    @GetMapping("/accounts/accountNo/{accountNo}")
    public ResponseEntity<CustomApiResponse<AccountResponse>> findAccountByAccountNo (@PathVariable String accountNo) {
        AccountResponse accountResponse = accountService.findAccountByAccountNo (accountNo);
        return ResponseEntity.ok (CustomApiResponse.success ("Account fetched successfully", accountResponse, HttpStatus.OK.value ()));
    }

    @Operation(summary = "Get all Accounts", description = "Retrieves all accounts")
    @GetMapping("/accounts")
    public ResponseEntity<CustomApiResponse<List<AccountResponse>>> getAllAccounts () {
        List<AccountResponse> accountResponses = accountService.getAllAccounts ();
        return ResponseEntity.ok (CustomApiResponse.success ("Accounts fetched successfully", accountResponses, HttpStatus.OK.value ()));
    }

    @Operation(summary = "Create UPI ID", description = "Creates a new UPI ID and returns the UPI details")
    @PostMapping("/upi")
    public ResponseEntity<CustomApiResponse<UpiResponse>> createUpi (UpiRequest upiRequest) {
        UpiResponse upiResponse = upiService.createUpi (upiRequest);
        return ResponseEntity.ok (CustomApiResponse.success ("UPI created successfully", upiResponse, HttpStatus.CREATED.value ()));
    }

    @Operation(summary = "Get UPI by ID", description = "Retrieves an UPI by its ID")
    @GetMapping("/upi/{id}")
    public ResponseEntity<CustomApiResponse<UpiResponse>> getUpiById (@PathVariable String id) {
        UpiResponse upiResponse = upiService.getUpiById (id);
        return ResponseEntity.ok (CustomApiResponse.success ("UPI fetched successfully", upiResponse, HttpStatus.OK.value ()));
    }

    @PostMapping("/upi/pay")
    public ResponseEntity<CustomApiResponse<Transaction>> payByUPI (@RequestBody PayByUpi paymentRequestDTO) {

        // Call the service method to process the UPI payment and retrieve the Transaction object
        Transaction transaction = upiService.payByUPI (
                paymentRequestDTO.getSenderUpiId (),
                paymentRequestDTO.getReceiverUpiId (),
                paymentRequestDTO.getSenderPin (),
                paymentRequestDTO.getAmount ()
        );

        // Return success response with the transaction details
        return ResponseEntity.ok ().body (CustomApiResponse.success ("", transaction, HttpStatus.OK.value ()));


    }

}

