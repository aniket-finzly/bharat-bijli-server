package com.finzly.bbc.services.payment;

import com.finzly.bbc.models.payment.*;
import com.finzly.bbc.repositories.payment.AccountRepository;
import com.finzly.bbc.repositories.payment.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void sendMoney (String senderAccountId, String receiverAccountId, Long amount, PaymentType paymentType) throws Exception {
        Account senderAccount = accountRepository.findById (senderAccountId)
                .orElseThrow (() -> new Exception ("Sender account not found"));
        Account receiverAccount = accountRepository.findById (receiverAccountId)
                .orElseThrow (() -> new Exception ("Receiver account not found"));

        if (senderAccount.getAccountBalance () < amount) {
            throw new Exception ("Insufficient balance in sender's account");
        }

        try {
            // Deduct amount from sender's account
            senderAccount.setAccountBalance (senderAccount.getAccountBalance () - amount);
            accountRepository.save (senderAccount);

            // Add amount to receiver's account
            receiverAccount.setAccountBalance (receiverAccount.getAccountBalance () + amount);
            accountRepository.save (receiverAccount);

            // Create and save transaction for sender
            Transaction senderTransaction = new Transaction ();
            senderTransaction.setSenderAccountId (senderAccountId);
            senderTransaction.setReceiverAccountId (receiverAccountId);
            senderTransaction.setAmount (amount);
            senderTransaction.setType (TransactionType.DEBIT);
            senderTransaction.setPaymentType (paymentType);
            senderTransaction.setStatus (PaymentStatus.SUCCESS);
            transactionRepository.save (senderTransaction);

            // Create and save transaction for receiver
            Transaction receiverTransaction = new Transaction ();
            receiverTransaction.setSenderAccountId (senderAccountId);
            receiverTransaction.setReceiverAccountId (receiverAccountId);
            receiverTransaction.setAmount (amount);
            receiverTransaction.setType (TransactionType.CREDIT);
            receiverTransaction.setPaymentType (paymentType);
            receiverTransaction.setStatus (PaymentStatus.SUCCESS);
            transactionRepository.save (receiverTransaction);
        } catch (Exception e) {
            // Handle the failure case
            logFailedTransaction (senderAccountId, receiverAccountId, amount, paymentType, e.getMessage ());
            throw new Exception ("Payment processing failed. Transaction has been logged.");
        }
    }

    private void logFailedTransaction (String senderAccountId, String receiverAccountId, Long amount, PaymentType paymentType, String errorMessage) {
        // Create and save failed transaction
        Transaction failedTransaction = new Transaction ();
        failedTransaction.setSenderAccountId (senderAccountId);
        failedTransaction.setReceiverAccountId (receiverAccountId);
        failedTransaction.setAmount (amount);
        failedTransaction.setType (TransactionType.DEBIT); // Adjust if needed
        failedTransaction.setPaymentType (paymentType);
        failedTransaction.setStatus (PaymentStatus.FAILED);
        transactionRepository.save (failedTransaction); // Save the failed transaction
    }
}
