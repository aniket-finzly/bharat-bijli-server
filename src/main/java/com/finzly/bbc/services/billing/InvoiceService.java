package com.finzly.bbc.services.billing;

import com.finzly.bbc.dtos.billing.InvoiceRequest;
import com.finzly.bbc.dtos.billing.InvoiceResponse;
import com.finzly.bbc.dtos.common.PaginationRequest;
import com.finzly.bbc.dtos.common.PaginationResponse;
import com.finzly.bbc.exceptions.BadRequestException;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.models.billing.Connection;
import com.finzly.bbc.models.billing.ConnectionType;
import com.finzly.bbc.models.billing.Invoice;
import com.finzly.bbc.repositories.billing.ConnectionRepository;
import com.finzly.bbc.repositories.billing.ConnectionTypeRepository;
import com.finzly.bbc.repositories.billing.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ConnectionRepository connectionRepository;
    private final ModelMapper modelMapper;
    private final ConnectionTypeRepository connectionTypeRepository;

    // Create a new invoice
    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest) {
        if (invoiceRequest.getConnectionId () == null || invoiceRequest.getDueDate () == null) {
            throw new BadRequestException ("Connection ID and due date are mandatory.");
        }

        if (invoiceRequest.getTotalUnits () == null) {
            throw new BadRequestException ("Total units are mandatory.");
        }

        Connection connection = connectionRepository.findById (invoiceRequest.getConnectionId ()).orElseThrow (
                () -> new ResourceNotFoundException ("Connection not found with ID: " + invoiceRequest.getConnectionId ()));

        ConnectionType connectionType = connectionTypeRepository.findById (connection.getConnectionType ().getId ()).orElseThrow (
                () -> new ResourceNotFoundException ("Connection Type not found with ID: " + connection.getConnectionType ().getId ())
        );


        Invoice invoice = modelMapper.map(invoiceRequest, Invoice.class);
        invoice.setTotalAmount (calculateTotalAmount (invoiceRequest.getTotalUnits ())); // Calculate total amount
        invoice.setTotalAmountAfterDiscount (calculateDiscountedAmount (invoice.getTotalAmount ())); // Apply discount
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return modelMapper.map(savedInvoice, InvoiceResponse.class);
    }

    // Read invoice by ID
    public InvoiceResponse getInvoiceById(String id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));
        return modelMapper.map(invoice, InvoiceResponse.class);
    }

    // Update invoice details
    public InvoiceResponse updateInvoice(String id, InvoiceRequest invoiceRequest) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));

        // Update fields if they are present in the request
        if (invoiceRequest.getDueDate() != null) {
            invoice.setDueDate(invoiceRequest.getDueDate());
        }
        if (invoiceRequest.getTotalUnits () != null) {
            invoice.setTotalUnits (invoiceRequest.getTotalUnits ());
            invoice.setTotalAmount (calculateTotalAmount (invoiceRequest.getTotalUnits ())); // Recalculate total amount
            invoice.setTotalAmountAfterDiscount (calculateDiscountedAmount (invoice.getTotalAmount ())); // Reapply discount
        }

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return modelMapper.map(updatedInvoice, InvoiceResponse.class);
    }

    // Delete invoice by ID
    public void deleteInvoice(String id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with ID: " + id));
        invoiceRepository.delete(invoice);
    }

    public PaginationResponse<InvoiceResponse> searchInvoicesWithPagination (
            String connectionId, LocalDateTime dueDate, PaginationRequest paginationRequest) {

        Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());

        Page<Invoice> invoicePage = invoiceRepository.searchInvoices (connectionId, dueDate, pageable);

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

    private BigDecimal calculateTotalAmount (BigDecimal totalUnits) {
        // Implement your logic to calculate total amount based on total units
        return totalUnits.multiply (new BigDecimal ("10")); // Example calculation
    }

    private BigDecimal calculateDiscountedAmount (BigDecimal totalAmount) {
        // Implement your logic to apply discounts if applicable
        BigDecimal discount = totalAmount.multiply (new BigDecimal ("0.05")); // Example 5% discount
        return totalAmount.subtract (discount);
    }
}
