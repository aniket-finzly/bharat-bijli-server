package com.finzly.bbc.controllers.payment;

import com.finzly.bbc.dto.payment.*;
import com.finzly.bbc.models.payment.*;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.payment.PaymentService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/accounts")
    public ResponseEntity<ApiResponse<Account>> createAccount (@RequestBody AccountDTO accountDTO, @RequestParam String userId) {
        Account account = paymentService.createAccount (accountDTO, userId);
        return new ResponseEntity<> (ApiResponse.success ("Account created successfully", account), HttpStatus.CREATED);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<ApiResponse<Account>> getAccountById (@PathVariable String id) {
        Account account = paymentService.getAccountById (id);
        return ResponseEntity.ok (ApiResponse.success ("Account retrieved successfully", account));
    }

    @SneakyThrows
    @GetMapping("/accounts")
    public ResponseEntity<ApiResponse<List<Account>>> getAllAccounts () {
        List<Account> accounts = paymentService.getAllAccounts ();
        return ResponseEntity.ok (ApiResponse.success ("Accounts retrieved successfully", accounts));
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<ApiResponse<Account>> updateAccount (@PathVariable String id, @RequestBody AccountDTO accountDTO) {
        Account updatedAccount = paymentService.updateAccount (id, accountDTO);
        return ResponseEntity.ok (ApiResponse.success ("Account updated successfully", updatedAccount));
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount (@PathVariable String id) {
        paymentService.deleteAccount (id);
        return ResponseEntity.ok (ApiResponse.success ("Account deleted successfully", null));
    }

    @SneakyThrows
    @PostMapping("/upi")
    public ResponseEntity<ApiResponse<Upi>> createUpi (@RequestBody UpiDTO upiDTO) {
        Upi upi = paymentService.createUpi (upiDTO);
        return new ResponseEntity<> (ApiResponse.success ("UPI created successfully", upi), HttpStatus.CREATED);
    }

    @SneakyThrows
    @GetMapping("/upi/{upiId}")
    public ResponseEntity<ApiResponse<Upi>> getUpiById (@PathVariable String upiId) {
        Upi upi = paymentService.getUpiById (upiId);
        return ResponseEntity.ok (ApiResponse.success ("UPI retrieved successfully", upi));
    }

    @SneakyThrows
    @PostMapping("/debit-card")
    public ResponseEntity<ApiResponse<DebitCard>> createDebitCard (@RequestBody DebitCardDTO debitCardDTO) {
        DebitCard debitCard = paymentService.createDebitCard (debitCardDTO);
        return new ResponseEntity<> (ApiResponse.success ("Debit Card created successfully", debitCard), HttpStatus.CREATED);
    }

    @SneakyThrows
    @GetMapping("/debit-card/{debitCardId}")
    public ResponseEntity<ApiResponse<DebitCard>> getDebitCardById (@PathVariable String debitCardId) {
        DebitCard debitCard = paymentService.getDebitCardById (debitCardId);
        return ResponseEntity.ok (ApiResponse.success ("Debit Card retrieved successfully", debitCard));
    }

    @SneakyThrows
    @PostMapping("/credit-card")
    public ResponseEntity<ApiResponse<CreditCard>> createCreditCard (@RequestBody CreditCardDTO creditCardDTO) {
        CreditCard creditCard = paymentService.createCreditCard (creditCardDTO);
        return new ResponseEntity<> (ApiResponse.success ("Credit Card created successfully", creditCard), HttpStatus.CREATED);
    }

    @SneakyThrows
    @GetMapping("/credit-card/{creditCardId}")
    public ResponseEntity<ApiResponse<CreditCard>> getCreditCardById (@PathVariable String creditCardId) {
        CreditCard creditCard = paymentService.getCreditCardById (creditCardId);
        return ResponseEntity.ok (ApiResponse.success ("Credit Card retrieved successfully", creditCard));
    }

    @SneakyThrows
    @PostMapping("/upi/payment")
    public ResponseEntity<ApiResponse<PaymentStatus>> upiPayment (@RequestBody UpiPaymentDTO upiPaymentDTO) {
        PaymentStatus paymentStatus = paymentService.upiPayment (upiPaymentDTO);
        return ResponseEntity.ok (ApiResponse.success ("UPI payment processed successfully", paymentStatus));
    }

}
