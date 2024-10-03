package com.finzly.bbc.services.payment;

import com.finzly.bbc.dto.payment.AccountDTO;
import com.finzly.bbc.exceptions.custom.payment.AccountException;
import com.finzly.bbc.exceptions.custom.payment.AccountNotFoundException;
import com.finzly.bbc.models.auth.User;
import com.finzly.bbc.models.payment.Account;
import com.finzly.bbc.repositories.auth.UserRepository;
import com.finzly.bbc.repositories.payment.AccountRepository;
import com.finzly.bbc.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.encryption.key}")
    private String encryptionKey;

    // Create a new account
    public Account createAccount (AccountDTO accountDTO, String userId) {
        try {
            User user = userRepository.findById (userId)
                    .orElseThrow (() -> new AccountException ("User not found with ID: " + userId));

            Account account = new Account ();
            account.setAccountNo (encryptAccountNo (accountDTO.getAccountNo ())); // Encrypt account number
            account.setBankName (accountDTO.getBankName ());
            account.setIfsc (accountDTO.getIfsc ());
            account.setAccountBalance (accountDTO.getAccountBalance ());
            account.setUser (user);

            return accountRepository.save (account);
        } catch (Exception e) {
            throw new AccountException ("Error creating account: " + e.getMessage ());
        }
    }

    // Retrieve account by ID
    public Account getAccountById (String id) {
        return accountRepository.findById (id)
                .orElseThrow (() -> new AccountNotFoundException ("Account with ID " + id + " not found"));
    }

    // Retrieve all accounts
    public List<Account> getAllAccounts () throws Exception {
        List<Account> accounts = accountRepository.findAll ();
        for (Account account : accounts) {
            account.setAccountNo (decryptAccountNo (account.getAccountNo ()));
        }
        return accounts;
    }

    // Update account details
    public Account updateAccount (String id, AccountDTO accountDTO) {
        try {
            Account existingAccount = getAccountById (id);

            existingAccount.setBankName (accountDTO.getBankName ());
            existingAccount.setIfsc (accountDTO.getIfsc ());
            existingAccount.setAccountBalance (accountDTO.getAccountBalance ());

            // Encrypt account number if it needs to be updated
            if (accountDTO.getAccountNo () != null) {
                existingAccount.setAccountNo (encryptAccountNo (accountDTO.getAccountNo ()));
            }

            return accountRepository.save (existingAccount);
        } catch (Exception e) {
            throw new AccountException ("Error updating account: " + e.getMessage ());
        }
    }

    // Delete account by ID
    public void deleteAccount (String id) {
        if (!accountRepository.existsById (id)) {
            throw new AccountNotFoundException ("Account with ID " + id + " not found");
        }
        accountRepository.deleteById (id);
    }

    // Helper methods for encryption and decryption
    private String encryptAccountNo (String accountNo) throws Exception {
        return EncryptionUtil.encrypt (accountNo, encryptionKey);
    }

    private String decryptAccountNo (String encryptedAccountNo) throws Exception {
        return EncryptionUtil.decrypt (encryptedAccountNo, encryptionKey);
    }
}
