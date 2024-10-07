package com.finzly.bbc.services.payment;

import com.finzly.bbc.dtos.payment.AccountRequest;
import com.finzly.bbc.dtos.payment.AccountResponse;
import com.finzly.bbc.exceptions.BadRequestException;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.models.payment.Account;
import com.finzly.bbc.repositories.auth.UserRepository;
import com.finzly.bbc.repositories.payment.AccountRepository;
import com.finzly.bbc.utils.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.encryption.key}")
    private String encryptionKey;

    // Method to create a new account from AccountRequest
    public AccountResponse createAccount (AccountRequest accountRequest) {
        User user = userRepository.findById (accountRequest.getUserId ()).orElseThrow (
                () -> new BadRequestException ("User not found with id: " + accountRequest.getUserId ())
        );

        Account account = new Account ();

        account.setBankName (accountRequest.getBankName ());
        account.setIfsc (accountRequest.getIfsc ());
        account.setBalance (accountRequest.getBalance ());

        // Generate and encrypt the account number
        String generatedAccountNo = account.generateAccountNo ();
        account.setAccountNo (encryptField (generatedAccountNo));
        account.setUser (user);

        accountRepository.save (account);

        return AccountResponse.builder ()
                .accountHolderEmail (user.getEmail ())
                .accountNo (decryptField (account.getAccountNo ())) // Decrypting for response
                .accountHolderName (user.getFirstName () + " " + user.getLastName ())
                .ifsc (account.getIfsc ())
                .bankName (account.getBankName ())
                .balance (account.getBalance ())
                .createdAt (account.getCreatedAt ())
                .id (account.getId ())
                .build ();
    }

    public AccountResponse getAccountById (String id) {
        Optional<Account> account = accountRepository.findById (id);
        if (account.isEmpty ()) {
            throw new BadRequestException ("Account not found with ID: " + id);
        }
        return buildAccountResponse (account.get ());
    }

    public AccountResponse findAccountByAccountNo (String accountNo) {
        log.info ("Encrypted Account No: {}", encryptField (accountNo));
        Optional<Account> account = accountRepository.findByAccountNo (encryptField (accountNo)); // Encrypt accountNo for search
        if (account.isEmpty ()) {
            throw new BadRequestException ("Account not found with Account No: " + accountNo);
        }
        return buildAccountResponse (account.get ());
    }

    private AccountResponse buildAccountResponse (Account account) {
        User user = account.getUser ();
        return AccountResponse.builder ()
                .accountHolderEmail (user.getEmail ())
                .accountNo (decryptField (account.getAccountNo ())) // Decrypting for response
                .accountHolderName (user.getFirstName () + " " + user.getLastName ())
                .ifsc (account.getIfsc ())
                .bankName (account.getBankName ())
                .balance (account.getBalance ())
                .createdAt (account.getCreatedAt ())
                .id (account.getId ())
                .build ();
    }


    public List<AccountResponse> getAllAccounts () {
        List<Account> accounts = accountRepository.findAll ();
        return accounts.stream ()
                .map (this::buildAccountResponse)
                .toList ();
    }

    private String encryptField (String data) {
        try {
            return EncryptionUtil.encrypt (data, encryptionKey);
        } catch (Exception e) {
            log.error ("Error encrypting data: {}", data, e);
            throw new RuntimeException ("Encryption error", e.getCause ());
        }
    }

    private String decryptField (String field) {
        try {
            return EncryptionUtil.decrypt (field, encryptionKey);
        } catch (Exception e) {
            throw new RuntimeException ("Decryption error", e);
        }
    }
}
