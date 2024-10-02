package com.finzly.bbc.dto.billing.mapper;

import com.finzly.bbc.dto.billing.PaymentTransactionDTO;
import com.finzly.bbc.models.billing.Invoice;
import com.finzly.bbc.models.billing.PaymentStatus;
import com.finzly.bbc.models.billing.PaymentTransaction;
import org.springframework.stereotype.Component;

// Mapper for PaymentTransaction entity
@Component
public class PaymentTransactionMapper {

    public PaymentTransactionDTO toPaymentTransactionDTO (PaymentTransaction paymentTransaction) {
        return new PaymentTransactionDTO (
                paymentTransaction.getId (),
                paymentTransaction.getInvoice ().getId (),
                paymentTransaction.getPaymentDate (),
                paymentTransaction.getPaymentAmount (),
                paymentTransaction.getPaymentMethod (),
                paymentTransaction.getPaymentStatus ().name ()
        );
    }

    public PaymentTransaction toPaymentTransactionEntity (PaymentTransactionDTO paymentTransactionDTO, Invoice invoice) {
        return new PaymentTransaction (
                paymentTransactionDTO.getId (),
                invoice,
                paymentTransactionDTO.getPaymentDate (),
                paymentTransactionDTO.getPaymentAmount (),
                paymentTransactionDTO.getPaymentMethod (),
                PaymentStatus.valueOf (paymentTransactionDTO.getPaymentStatus ())
        );
    }

    public void updatePaymentTransactionEntity (PaymentTransaction paymentTransaction, PaymentTransactionDTO paymentTransactionDTO) {
        // Update the existing PaymentTransaction entity with new values
        paymentTransaction.setPaymentDate (paymentTransactionDTO.getPaymentDate ());
        paymentTransaction.setPaymentAmount (paymentTransactionDTO.getPaymentAmount ());
        paymentTransaction.setPaymentMethod (paymentTransactionDTO.getPaymentMethod ());
        paymentTransaction.setPaymentStatus (PaymentStatus.valueOf (paymentTransactionDTO.getPaymentStatus ()));
    }
}
