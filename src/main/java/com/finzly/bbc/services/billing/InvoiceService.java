package com.finzly.bbc.services.billing;

import com.finzly.bbc.dtos.billing.InvoiceRequest;
import com.finzly.bbc.dtos.billing.InvoiceResponse;
import com.finzly.bbc.dtos.common.PaginationRequest;
import com.finzly.bbc.dtos.common.PaginationResponse;
import com.finzly.bbc.exceptions.BadRequestException;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.models.billing.Invoice;
import com.finzly.bbc.models.billing.PaymentStatus;
import com.finzly.bbc.repositories.billing.InvoiceRepository;
import com.finzly.bbc.repositories.billing.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ConnectionRepository connectionRepository;
    private final ModelMapper modelMapper;

    // Create a new invoice
    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest) {
        // Validate that the connection exists
        connectionRepository.findById(invoiceRequest.getConnectionId())
                .orElseThrow(() -> new BadRequestException("Connection not found with ID: " + invoiceRequest.getConnectionId()));

        // Map request to Invoice entity
        Invoice invoice = modelMapper.map(invoiceRequest, Invoice.class);
        invoice.setPaymentStatus(PaymentStatus.PENDING); // Default status on creation

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return modelMapper.map(savedInvoice, InvoiceResponse.class);
    }

    // Retrieve an invoice by ID
    public InvoiceResponse getInvoiceById(String id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));
        return modelMapper.map(invoice, InvoiceResponse.class);
    }

    // Update invoice details
    public InvoiceResponse updateInvoice(String id, InvoiceRequest invoiceRequest) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));

        if (invoiceRequest.getBillingPeriodStart() != null) {
            invoice.setBillingPeriodStart(invoiceRequest.getBillingPeriodStart());
        }
        if (invoiceRequest.getBillingPeriodEnd() != null) {
            invoice.setBillingPeriodEnd(invoiceRequest.getBillingPeriodEnd());
        }
        if (invoiceRequest.getTotalUnits() != null) {
            invoice.setTotalUnits(invoiceRequest.getTotalUnits());
        }
        if (invoiceRequest.getTotalAmount() != null) {
            invoice.setTotalAmount(invoiceRequest.getTotalAmount());
        }
        if (invoiceRequest.getDueDate() != null) {
            invoice.setDueDate(invoiceRequest.getDueDate());
        }

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return modelMapper.map(updatedInvoice, InvoiceResponse.class);
    }

    // Delete an invoice by ID
    public void deleteInvoice(String id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));
        invoiceRepository.delete(invoice);
    }

    // Search invoices with pagination
    public PaginationResponse<InvoiceResponse> searchInvoicesWithPagination(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());
        Page<Invoice> invoicePage = invoiceRepository.findAll(pageable);

        List<InvoiceResponse> invoiceResponses = invoicePage.getContent().stream()
                .map(invoice -> modelMapper.map(invoice, InvoiceResponse.class))
                .toList();

        return PaginationResponse.<InvoiceResponse>builder()
                .content(invoiceResponses)
                .totalPages(invoicePage.getTotalPages())
                .totalElements(invoicePage.getTotalElements())
                .size(invoicePage.getSize())
                .number(invoicePage.getNumber())
                .build();
    }
}
