package com.finzly.bbc.services.payment;

import com.finzly.bbc.exceptions.custom.payment.DebitCardNotFoundException;
import com.finzly.bbc.models.payment.Account;
import com.finzly.bbc.models.payment.DebitCard;
import com.finzly.bbc.repositories.payment.AccountRepository;
import com.finzly.bbc.repositories.payment.DebitCardRepository;
import com.finzly.bbc.utils.EncryptionUtil;
import com.finzly.bbc.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Value("${app.encryption.key}") // Encryption key from application properties
    private String encryptionKey;

    // Create a new Debit Card
    public DebitCard createDebitCard (String accountId, String pin) throws Exception {
        Account account = accountRepository.findById (accountId)
                .orElseThrow (() -> new DebitCardNotFoundException ("Account with ID " + accountId + " not found"));

        DebitCard debitCard = new DebitCard ();
        debitCard.setNumber (encryptDebitCardNumber (generateDebitCardNumber ()));
        debitCard.setPin (encryptPin (pin));
        debitCard.setCvv (RandomUtil.generateRandomNumericString (3)); // Generate CVV
        debitCard.setExpiryDate (LocalDate.now ().plusYears (5)); // Set expiry date to 5 years from now
        debitCard.setAccount (account);

        return debitCardRepository.save (debitCard);
    }

    // Retrieve Debit Card by ID
    public DebitCard getDebitCardById (String debitCardId) throws Exception {
        DebitCard debitCard = debitCardRepository.findById (debitCardId)
                .orElseThrow (() -> new DebitCardNotFoundException ("Debit Card with ID " + debitCardId + " not found"));

        // Decrypt sensitive fields before returning
        debitCard.setNumber (decryptDebitCardNumber (debitCard.getNumber ()));
        debitCard.setPin (decryptPin (debitCard.getPin ()));
        return debitCard;
    }

    // Retrieve all Debit Cards
    public List<DebitCard> getAllDebitCards () throws Exception {
        List<DebitCard> debitCards = debitCardRepository.findAll ();
        // Decrypt sensitive fields before returning
        for (DebitCard debitCard : debitCards) {
            debitCard.setNumber (decryptDebitCardNumber (debitCard.getNumber ()));
            debitCard.setPin (decryptPin (debitCard.getPin ()));
        }
        return debitCards;
    }

    // Update Debit Card details
    public DebitCard updateDebitCard (String debitCardId, DebitCard updatedDebitCard) throws Exception {
        DebitCard existingDebitCard = getDebitCardById (debitCardId);
        existingDebitCard.setPin (encryptPin (updatedDebitCard.getPin ()));
        existingDebitCard.setCvv (updatedDebitCard.getCvv ());
        existingDebitCard.setExpiryDate (updatedDebitCard.getExpiryDate ());
        return debitCardRepository.save (existingDebitCard);
    }

    // Delete Debit Card by ID
    public void deleteDebitCard (String debitCardId) {
        if (!debitCardRepository.existsById (debitCardId)) {
            throw new DebitCardNotFoundException ("Debit Card with ID " + debitCardId + " not found");
        }
        debitCardRepository.deleteById (debitCardId);
    }

    // Generate a random 16-digit debit card number
    private String generateDebitCardNumber () {
        return RandomUtil.generateRandomNumericString (16);
    }

    // Helper methods for encryption and decryption
    private String encryptDebitCardNumber (String debitCardNumber) throws Exception {
        return EncryptionUtil.encrypt (debitCardNumber, encryptionKey);
    }

    private String decryptDebitCardNumber (String encryptedDebitCardNumber) throws Exception {
        return EncryptionUtil.decrypt (encryptedDebitCardNumber, encryptionKey);
    }

    private String encryptPin (String pin) throws Exception {
        return EncryptionUtil.encrypt (pin, encryptionKey);
    }

    private String decryptPin (String encryptedPin) throws Exception {
        return EncryptionUtil.decrypt (encryptedPin, encryptionKey);
    }
}
