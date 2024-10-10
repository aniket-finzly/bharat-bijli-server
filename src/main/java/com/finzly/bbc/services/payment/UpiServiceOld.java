package com.finzly.bbc.services.payment;

import org.springframework.stereotype.Service;

@Service
public class UpiServiceOld {
//
//    @Autowired
//    private UpiRepository upiRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private TransactionService transactionService;
//
//
//    @Value("${app.encryption.key}") // Encryption key from application properties
//    private String encryptionKey;
//
//    // Create a new UPI ID
//    public Upi createUpi (String accountId, String upiId, String pin) throws Exception {
//        Account account = accountRepository.findById (accountId)
//                .orElseThrow (() -> new ResourceNotFoundException ("Account with ID " + accountId + " not found"));
//
//        Upi upi = new Upi ();
//        upi.setUpiId (encryptUpiId (upiId));
//        upi.setPin (encryptPin (pin));
//        upi.setAccount (account);
//
//        return upiRepository.save (upi);
//    }
//
//    // Retrieve UPI by ID
//    public Upi getUpiById (String upiId) throws Exception {
//        Upi upi = upiRepository.findById (upiId)
//                .orElseThrow (() -> new ResourceNotFoundException ("UPI with ID " + upiId + " not found"));
//
//        // Decrypt sensitive fields before returning
//        upi.setUpiId (decryptUpiId (upi.getUpiId ()));
//        upi.setPin (decryptPin (upi.getPin ()));
//        return upi;
//    }
//
//    // Retrieve all UPI IDs
//    public List<Upi> getAllUpis () throws Exception {
//        List<Upi> upis = upiRepository.findAll ();
//        // Decrypt sensitive fields before returning
//        for (Upi upi : upis) {
//            upi.setUpiId (decryptUpiId (upi.getUpiId ()));
//            upi.setPin (decryptPin (upi.getPin ()));
//        }
//        return upis;
//    }
//
//    // Update UPI details
//    public Upi updateUpi (String upiId, Upi updatedUpi) throws Exception {
//        Upi existingUpi = getUpiById (upiId);
//        existingUpi.setUpiId (encryptUpiId (updatedUpi.getUpiId ()));
//        existingUpi.setPin (encryptPin (updatedUpi.getPin ()));
//        return upiRepository.save (existingUpi);
//    }
//
//    // Delete UPI by ID
//    public void deleteUpi (String upiId) {
//        if (!upiRepository.existsById (upiId)) {
//            throw new ResourceNotFoundException ("UPI with ID " + upiId + " not found");
//        }
//        upiRepository.deleteById (upiId);
//    }
//
//    // Helper methods for encryption and decryption
//    private String encryptUpiId (String upiId) throws Exception {
//        return EncryptionUtil.encrypt (upiId, encryptionKey);
//    }
//
//    private String decryptUpiId (String encryptedUpiId) throws Exception {
//        return EncryptionUtil.decrypt (encryptedUpiId, encryptionKey);
//    }
//
//    private String encryptPin (String pin) throws Exception {
//        return EncryptionUtil.encrypt (pin, encryptionKey);
//    }
//
//    private String decryptPin (String encryptedPin) throws Exception {
//        return EncryptionUtil.decrypt (encryptedPin, encryptionKey);
//    }
//
//    public TransactPaymentStatus handleUpiPayment (UpiPaymentDTO upiPaymentDTO) throws Exception {
//        Upi sender = upiRepository.findByUpiId (upiPaymentDTO.getSenderUpiId ());
//        Upi receiver = upiRepository.findByUpiId (upiPaymentDTO.getReceiverUpiId ());
//
//        if (sender == null || receiver == null) {
//            throw new RuntimeException ("Invalid UPI IDs");
//        }
//
//        if (!sender.getPin ().equals (decryptPin (upiPaymentDTO.getSenderPin ()))) {
//            throw new RuntimeException ("Invalid PIN");
//        }
//
//        if (sender.getAccount ().getAccountBalance () < upiPaymentDTO.getAmount ()) {
//            throw new RuntimeException ("Insufficient balance");
//        }
//
//        // Use the TransactionService to handle the payment transfer
//        transactionService.sendMoney (sender.getAccount ().getId (), receiver.getAccount ().getId (), upiPaymentDTO.getAmount (), PaymentType.UPI);
//
//        // Send email notifications
//        sendPaymentEmails (sender, receiver, upiPaymentDTO.getAmount ());
//
//        return TransactPaymentStatus.SUCCESS;
//    }
//
//    private void sendPaymentEmails (Upi sender, Upi receiver, Long amount) {
//        String senderEmail = sender.getAccount ().getUser ().getEmail ();
//        String receiverEmail = receiver.getAccount ().getUser ().getEmail ();
//
//        emailService.sendEmail (senderEmail, "Account Debited", "Your payment of " + amount + " has been debited and credited to UPI " + receiver.getUpiId (), false);
//        emailService.sendEmail (receiverEmail, "Payment Received", "Your payment of " + amount + " has been successfully received from UPI " + sender.getUpiId (), false);
//    }

}


