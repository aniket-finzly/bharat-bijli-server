package com.finzly.bbc.dto.billing.mapper;

import com.finzly.bbc.dto.billing.InvoiceDTO;
import com.finzly.bbc.models.billing.Connection;
import com.finzly.bbc.models.billing.Invoice;
import com.finzly.bbc.models.billing.PaymentStatus;
import org.springframework.stereotype.Component;

// Mapper for Invoice entity
@Component
public class InvoiceMapper {

    public InvoiceDTO toInvoiceDTO (Invoice invoice) {
        return new InvoiceDTO (
                invoice.getId (),
                invoice.getConnection ().getId (),
                invoice.getBillingPeriodStart (),
                invoice.getBillingPeriodEnd (),
                invoice.getTotalUnits (),
                invoice.getTotalAmount (),
                invoice.getPaymentStatus ().name (),
                invoice.getDueDate ()
        );
    }

    public Invoice toInvoiceEntity (InvoiceDTO invoiceDTO, Connection connection) {
        return new Invoice (
                invoiceDTO.getId (),
                connection,
                invoiceDTO.getBillingPeriodStart (),
                invoiceDTO.getBillingPeriodEnd (),
                invoiceDTO.getTotalUnits (),
                invoiceDTO.getTotalAmount (),
                PaymentStatus.valueOf (invoiceDTO.getPaymentStatus ()),
                invoiceDTO.getDueDate (),
                null, // assuming createdAt is handled elsewhere
                null  // assuming updatedAt is handled elsewhere
        );
    }

    public void updateInvoiceEntity (Invoice invoice, InvoiceDTO invoiceDTO) {
        // Update the existing Invoice entity with new values
        invoice.setBillingPeriodStart (invoiceDTO.getBillingPeriodStart ());
        invoice.setBillingPeriodEnd (invoiceDTO.getBillingPeriodEnd ());
        invoice.setTotalUnits (invoiceDTO.getTotalUnits ());
        invoice.setTotalAmount (invoiceDTO.getTotalAmount ());
        invoice.setPaymentStatus (PaymentStatus.valueOf (invoiceDTO.getPaymentStatus ()));
        invoice.setDueDate (invoiceDTO.getDueDate ());
        // Note: createdAt and updatedAt are assumed to be handled elsewhere
    }
}
