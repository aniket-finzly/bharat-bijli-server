package com.finzly.bbc.dto.billing.mapper;

import com.finzly.bbc.dto.billing.InvoiceItemDTO;
import com.finzly.bbc.models.billing.Invoice;
import com.finzly.bbc.models.billing.InvoiceItem;
import com.finzly.bbc.models.billing.Tariff;
import org.springframework.stereotype.Component;

// Mapper for InvoiceItem entity
@Component
public class InvoiceItemMapper {

    public InvoiceItemDTO toInvoiceItemDTO (InvoiceItem invoiceItem) {
        return new InvoiceItemDTO (
                invoiceItem.getId (),
                invoiceItem.getInvoice ().getId (),
                invoiceItem.getTariff ().getId (),
                invoiceItem.getUnitsConsumed (),
                invoiceItem.getChargeAmount (),
                invoiceItem.getDescription ()
        );
    }

    public InvoiceItem toInvoiceItemEntity (InvoiceItemDTO invoiceItemDTO, Invoice invoice, Tariff tariff) {
        return new InvoiceItem (
                invoiceItemDTO.getId (),
                invoice,
                tariff,
                invoiceItemDTO.getUnitsConsumed (),
                invoiceItemDTO.getChargeAmount (),
                invoiceItemDTO.getDescription ()
        );
    }

    public void updateInvoiceItemEntity (InvoiceItem invoiceItem, InvoiceItemDTO invoiceItemDTO) {
        // Update the existing InvoiceItem entity with new values
        invoiceItem.setUnitsConsumed (invoiceItemDTO.getUnitsConsumed ());
        invoiceItem.setChargeAmount (invoiceItemDTO.getChargeAmount ());
        invoiceItem.setDescription (invoiceItemDTO.getDescription ());
        // Note: The relationships (Invoice and Tariff) are assumed to be handled elsewhere
    }
}
