package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dto.billing.InvoiceItemDTO;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.billing.InvoiceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice-items")
public class InvoiceItemController {

    private final InvoiceItemService invoiceItemService;

    @Autowired
    public InvoiceItemController (InvoiceItemService invoiceItemService) {
        this.invoiceItemService = invoiceItemService;
    }

    // Get all invoice items
    @GetMapping
    public ApiResponse<List<InvoiceItemDTO>> getAllInvoiceItems () {
        List<InvoiceItemDTO> invoiceItems = invoiceItemService.getAllInvoiceItems ();
        return ApiResponse.success ("Fetched all invoice items successfully", invoiceItems);
    }

    // Get invoice item by ID
    @GetMapping("/{id}")
    public ApiResponse<InvoiceItemDTO> getInvoiceItemById (@PathVariable String id) {
        return invoiceItemService.getInvoiceItemById (id)
                .map (invoiceItemDTO -> ApiResponse.success ("Fetched invoice item successfully", invoiceItemDTO))
                .orElseGet (() -> ApiResponse.error ("Invoice item not found", 404));
    }

    // Create a new invoice item
    @PostMapping
    public ApiResponse<InvoiceItemDTO> createInvoiceItem (@RequestBody InvoiceItemDTO invoiceItemDTO) {
        InvoiceItemDTO createdInvoiceItem = invoiceItemService.createInvoiceItem (invoiceItemDTO);
        return ApiResponse.success ("Created invoice item successfully", createdInvoiceItem);
    }

    // Update an existing invoice item
    @PutMapping("/{id}")
    public ApiResponse<InvoiceItemDTO> updateInvoiceItem (@PathVariable String id, @RequestBody InvoiceItemDTO invoiceItemDTO) {
        InvoiceItemDTO updatedInvoiceItem = invoiceItemService.updateInvoiceItem (id, invoiceItemDTO);
        return ApiResponse.success ("Updated invoice item successfully", updatedInvoiceItem);
    }

    // Delete an invoice item
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteInvoiceItem (@PathVariable String id) {
        invoiceItemService.deleteInvoiceItem (id);
        return ApiResponse.success ("Deleted invoice item successfully", null);
    }

    // Find invoice items by invoice ID
    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<List<InvoiceItemDTO>> findInvoiceItemsByInvoiceId (@PathVariable String invoiceId) {
        List<InvoiceItemDTO> invoiceItems = invoiceItemService.findInvoiceItemsByInvoiceId (invoiceId);
        return ApiResponse.success ("Fetched invoice items for invoice successfully", invoiceItems);
    }
}



