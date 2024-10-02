package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dto.billing.InvoiceDTO;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.billing.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@Tag(name = "Invoice API", description = "API for managing invoices: create, update, delete, retrieve invoices.")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController (InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // Get all invoices
    @GetMapping
    @Operation(summary = "Get all invoices",
            description = "Retrieves a list of all invoices.")
    public ApiResponse<List<InvoiceDTO>> getAllInvoices () {
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices ();
        return ApiResponse.success ("Fetched all invoices successfully", invoices);
    }

    // Get invoice by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by ID",
            description = "Retrieves a specific invoice by its ID.")
    public ApiResponse<InvoiceDTO> getInvoiceById (@PathVariable String id) {
        return invoiceService.getInvoiceById (id)
                .map (invoiceDTO -> ApiResponse.success ("Fetched invoice successfully", invoiceDTO))
                .orElse (ApiResponse.error ("Invoice not found", 404));
    }

    // Create a new invoice
    @PostMapping
    @Operation(summary = "Create a new invoice",
            description = "Creates a new invoice with the provided details.")
    public ApiResponse<InvoiceDTO> createInvoice (@RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO createdInvoice = invoiceService.createInvoice (invoiceDTO);
        return ApiResponse.success ("Created invoice successfully", createdInvoice);
    }

    // Update an existing invoice
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing invoice",
            description = "Updates the details of an existing invoice.")
    public ApiResponse<InvoiceDTO> updateInvoice (@PathVariable String id, @RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO updatedInvoice = invoiceService.updateInvoice (id, invoiceDTO);
        return ApiResponse.success ("Updated invoice successfully", updatedInvoice);
    }

    // Delete an invoice
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an invoice",
            description = "Deletes the invoice identified by the specified ID.")
    public ApiResponse<Void> deleteInvoice (@PathVariable String id) {
        invoiceService.deleteInvoice (id);
        return ApiResponse.success ("Deleted invoice successfully", null);
    }

    // Get invoices by connection ID
    @GetMapping("/connection/{connectionId}")
    @Operation(summary = "Get invoices by connection ID",
            description = "Retrieves a list of invoices associated with a specific connection ID.")
    public ApiResponse<List<InvoiceDTO>> getInvoicesByConnectionId (@PathVariable String connectionId) {
        List<InvoiceDTO> invoices = invoiceService.getInvoicesByConnectionId (connectionId);
        return ApiResponse.success ("Fetched invoices for connection successfully", invoices);
    }

    // Get overdue invoices
    @GetMapping("/overdue")
    @Operation(summary = "Get overdue invoices",
            description = "Retrieves a list of overdue invoices.")
    public ApiResponse<List<InvoiceDTO>> getOverdueInvoices () {
        List<InvoiceDTO> overdueInvoices = invoiceService.getOverdueInvoices ();
        return ApiResponse.success ("Fetched overdue invoices successfully", overdueInvoices);
    }
}
