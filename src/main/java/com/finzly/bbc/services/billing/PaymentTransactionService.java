package com.finzly.bbc.services.billing;

import com.finzly.bbc.dto.billing.PaymentTransactionDTO;
import com.finzly.bbc.dto.billing.mapper.PaymentTransactionMapper;
import com.finzly.bbc.models.billing.Invoice;
import com.finzly.bbc.models.billing.PaymentTransaction;
import com.finzly.bbc.repositories.billing.InvoiceRepository;
import com.finzly.bbc.repositories.billing.PaymentTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service for PaymentTransaction entity
@Service
@Transactional
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentTransactionMapper paymentTransactionMapper;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public PaymentTransactionService (PaymentTransactionRepository paymentTransactionRepository,
                                      PaymentTransactionMapper paymentTransactionMapper, InvoiceRepository invoiceRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentTransactionMapper = paymentTransactionMapper;
        this.invoiceRepository = invoiceRepository;
    }

    // CRUD Operations
    public List<PaymentTransactionDTO> getAllPaymentTransactions () {
        return paymentTransactionRepository.findAll ().stream ()
                .map (paymentTransactionMapper::toPaymentTransactionDTO)
                .collect (Collectors.toList ());
    }

    public Optional<PaymentTransactionDTO> getPaymentTransactionById (String id) {
        return paymentTransactionRepository.findById (id).map (paymentTransactionMapper::toPaymentTransactionDTO);
    }

    public PaymentTransactionDTO createPaymentTransaction (PaymentTransactionDTO paymentTransactionDTO) {
        Invoice invoice = invoiceRepository.findById (paymentTransactionDTO.getInvoiceId ()).orElseThrow ();
        PaymentTransaction paymentTransaction = paymentTransactionMapper.toPaymentTransactionEntity (paymentTransactionDTO, invoice);
        return paymentTransactionMapper.toPaymentTransactionDTO (paymentTransactionRepository.save (paymentTransaction));
    }

    public PaymentTransactionDTO updatePaymentTransaction (String id, PaymentTransactionDTO paymentTransactionDTO) {
        PaymentTransaction paymentTransaction = paymentTransactionRepository.findById (id).orElseThrow ();
        paymentTransactionMapper.updatePaymentTransactionEntity (paymentTransaction, paymentTransactionDTO);
        return paymentTransactionMapper.toPaymentTransactionDTO (paymentTransactionRepository.save (paymentTransaction));
    }

    public void deletePaymentTransaction (String id) {
        paymentTransactionRepository.deleteById (id);
    }

    // Searching and Filtering
    public List<PaymentTransactionDTO> findTransactionsByInvoiceId (String invoiceId) {
        return paymentTransactionRepository.findByInvoiceId (invoiceId).stream ()
                .map (paymentTransactionMapper::toPaymentTransactionDTO)
                .collect (Collectors.toList ());
    }
}
