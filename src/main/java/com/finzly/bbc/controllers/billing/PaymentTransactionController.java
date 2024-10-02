package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dto.billing.PaymentTransactionDTO;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.billing.PaymentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-transactions")
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @Autowired
    public PaymentTransactionController (PaymentTransactionService paymentTransactionService) {
        this.paymentTransactionService = paymentTransactionService;
    }

    // Get all payment transactions
    @GetMapping
    public ApiResponse<List<PaymentTransactionDTO>> getAllPaymentTransactions () {
        List<PaymentTransactionDTO> transactions = paymentTransactionService.getAllPaymentTransactions ();
        return ApiResponse.success ("Fetched all payment transactions successfully", transactions);
    }

    // Get payment transaction by ID
    @GetMapping("/{id}")
    public ApiResponse<PaymentTransactionDTO> getPaymentTransactionById (@PathVariable String id) {
        return paymentTransactionService.getPaymentTransactionById (id)
                .map (transactionDTO -> ApiResponse.success ("Fetched payment transaction successfully", transactionDTO))
                .orElseGet (() -> ApiResponse.error ("Payment transaction not found", 404));
    }

    // Create a new payment transaction
    @PostMapping
    public ApiResponse<PaymentTransactionDTO> createPaymentTransaction (@RequestBody PaymentTransactionDTO paymentTransactionDTO) {
        PaymentTransactionDTO createdTransaction = paymentTransactionService.createPaymentTransaction (paymentTransactionDTO);
        return ApiResponse.success ("Created payment transaction successfully", createdTransaction);
    }

    // Update an existing payment transaction
    @PutMapping("/{id}")
    public ApiResponse<PaymentTransactionDTO> updatePaymentTransaction (@PathVariable String id, @RequestBody PaymentTransactionDTO paymentTransactionDTO) {
        PaymentTransactionDTO updatedTransaction = paymentTransactionService.updatePaymentTransaction (id, paymentTransactionDTO);
        return ApiResponse.success ("Updated payment transaction successfully", updatedTransaction);
    }

    // Delete a payment transaction
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePaymentTransaction (@PathVariable String id) {
        paymentTransactionService.deletePaymentTransaction (id);
        return ApiResponse.success ("Deleted payment transaction successfully", null);
    }

    // Get transactions by invoice ID
    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<List<PaymentTransactionDTO>> findTransactionsByInvoiceId (@PathVariable String invoiceId) {
        List<PaymentTransactionDTO> transactions = paymentTransactionService.findTransactionsByInvoiceId (invoiceId);
        return ApiResponse.success ("Fetched transactions for invoice successfully", transactions);
    }
}
