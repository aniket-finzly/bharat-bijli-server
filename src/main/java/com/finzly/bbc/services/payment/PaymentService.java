package com.finzly.bbc.services.payment;

import com.finzly.bbc.dto.payment.*;
import com.finzly.bbc.models.payment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private DebitCardService debitCardService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UpiService upiService;

    // Account service methods
    public Account createAccount (AccountDTO accountDTO, String userId) {
        return accountService.createAccount (accountDTO, userId);
    }

    public Account getAccountById (String id) {
        return accountService.getAccountById (id);
    }

    public List<Account> getAllAccounts () throws Exception {
        return accountService.getAllAccounts ();
    }

    public Account updateAccount (String id, AccountDTO accountDTO) {
        return accountService.updateAccount (id, accountDTO);
    }

    public void deleteAccount (String id) {
        accountService.deleteAccount (id);
    }

    public Upi createUpi (UpiDTO upiDTO) throws Exception {
        return upiService.createUpi (upiDTO.getAccountId (), upiDTO.getUpiId (), upiDTO.getPin ());
    }

    public Upi getUpiById (String upiId) throws Exception {
        return upiService.getUpiById (upiId);
    }

    public DebitCard createDebitCard (DebitCardDTO debitCardDTO) throws Exception {
        return debitCardService.createDebitCard (debitCardDTO.getAccountId (), debitCardDTO.getPin ());
    }

    public DebitCard getDebitCardById (String debitCardId) throws Exception {
        return debitCardService.getDebitCardById (debitCardId);
    }

    public CreditCard createCreditCard (CreditCardDTO creditCardDTO) throws Exception {
        return creditCardService.createCreditCard (
                creditCardDTO.getPin (),
                creditCardDTO.getLimit (),
                creditCardDTO.getCvv (),
                creditCardDTO.getExpiryTime (),
                creditCardDTO.getAccountId ()
        );
    }

    public CreditCard getCreditCardById (String creditCardId) throws Exception {
        return creditCardService.getCreditCardById (creditCardId);
    }


    public PaymentStatus upiPayment (UpiPaymentDTO upiPaymentDTO) throws Exception {
        return upiService.handleUpiPayment (upiPaymentDTO);
    }

}


