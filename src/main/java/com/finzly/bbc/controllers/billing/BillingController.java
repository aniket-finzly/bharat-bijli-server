package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.constants.InvoiceTransactionStatus;
import com.finzly.bbc.dtos.billing.*;
import com.finzly.bbc.dtos.common.PaginationRequest;
import com.finzly.bbc.dtos.common.PaginationResponse;
import com.finzly.bbc.models.payment.Transaction;
import com.finzly.bbc.response.CustomApiResponse;
import com.finzly.bbc.services.billing.ConnectionService;
import com.finzly.bbc.services.billing.ConnectionTypeService;
import com.finzly.bbc.services.billing.InvoiceService;
import com.finzly.bbc.services.billing.TariffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/billing/connections")
@RequiredArgsConstructor
@Tag(name = "Billing API for Connections", description = "APIs for managing connections, connection types, tariffs, and invoices")
public class BillingController {

    private final ConnectionService connectionService;
    private final ConnectionTypeService connectionTypeService;
    private final InvoiceService invoiceService;
    private final TariffService tariffService;

    // ------------------------ Connection Management ------------------------

    @PostMapping
    @Operation(summary = "Create Connection", description = "Create a new connection")
    public ResponseEntity<CustomApiResponse<ConnectionResponse>> createConnection (
            @RequestBody @Valid ConnectionRequest connectionRequest) {
        ConnectionResponse connectionResponse = connectionService.createConnection (connectionRequest);
        return ResponseEntity.status (HttpStatus.CREATED)
                .body (CustomApiResponse.success ("Connection created successfully", connectionResponse, HttpStatus.CREATED.value ()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Connection by ID", description = "Retrieve a connection by its ID")
    public ResponseEntity<CustomApiResponse<ConnectionResponse>> getConnectionById (@PathVariable String id) {
        ConnectionResponse connectionResponse = connectionService.getConnectionById (id);
        return ResponseEntity.ok (CustomApiResponse.success ("Connection fetched successfully", connectionResponse, HttpStatus.OK.value ()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Connection", description = "Update connection details by ID")
    public ResponseEntity<CustomApiResponse<ConnectionResponse>> updateConnection (
            @PathVariable String id,
            @RequestBody @Valid ConnectionRequest connectionRequest) {
        ConnectionResponse connectionResponse = connectionService.updateConnection (id, connectionRequest);
        return ResponseEntity.ok (CustomApiResponse.success ("Connection updated successfully", connectionResponse, HttpStatus.OK.value ()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Connection", description = "Delete a connection by its ID")
    public ResponseEntity<CustomApiResponse<String>> deleteConnection (@PathVariable String id) {
        connectionService.deleteConnection (id);
        return ResponseEntity.ok (CustomApiResponse.success ("Connection deleted successfully", null, HttpStatus.OK.value ()));
    }

    @GetMapping
    @Operation(summary = "Search Connections with Pagination", description = "Search connections with optional filters and pagination")
    public ResponseEntity<CustomApiResponse<PaginationResponse<ConnectionResponse>>> searchConnections (
            @ModelAttribute PaginationRequest paginationRequest,
            @RequestParam(value = "customerId", required = false) String customerId,
            @RequestParam(value = "connectionTypeId", required = false) String connectionTypeId,
            @RequestParam(value = "status", required = false) String status) {

        if (paginationRequest.getPage () == null) paginationRequest.setPage (0);
        if (paginationRequest.getSize () == null) paginationRequest.setSize (10);

        PaginationResponse<ConnectionResponse> response = connectionService.searchConnectionsWithPagination (
                customerId, connectionTypeId, status, paginationRequest
        );

        return ResponseEntity.ok (CustomApiResponse.success ("Connections fetched successfully", response, HttpStatus.OK.value ()));
    }
    // ------------------------ Connection Type Management ------------------------

    @PostMapping("/types")
    @Operation(summary = "Create Connection Type", description = "Create a new connection type")
    public ResponseEntity<CustomApiResponse<ConnectionTypeResponse>> createConnectionType (
            @RequestBody @Valid ConnectionTypeRequest connectionTypeRequest) {
        ConnectionTypeResponse connectionTypeResponse = connectionTypeService.createConnectionType (connectionTypeRequest);
        return ResponseEntity.status (HttpStatus.CREATED)
                .body (CustomApiResponse.success ("Connection type created successfully", connectionTypeResponse, HttpStatus.CREATED.value ()));
    }

    @GetMapping("/types/{id}")
    @Operation(summary = "Get Connection Type by ID", description = "Retrieve a connection type by its ID")
    public ResponseEntity<CustomApiResponse<ConnectionTypeResponse>> getConnectionTypeById (@PathVariable String id) {
        ConnectionTypeResponse connectionTypeResponse = connectionTypeService.getConnectionTypeById (id);
        return ResponseEntity.ok (CustomApiResponse.success ("Connection type fetched successfully", connectionTypeResponse, HttpStatus.OK.value ()));
    }

    @PutMapping("/types/{id}")
    @Operation(summary = "Update Connection Type", description = "Update connection type details by ID")
    public ResponseEntity<CustomApiResponse<ConnectionTypeResponse>> updateConnectionType (
            @PathVariable String id,
            @RequestBody @Valid ConnectionTypeRequest connectionTypeRequest) {
        ConnectionTypeResponse connectionTypeResponse = connectionTypeService.updateConnectionType (id, connectionTypeRequest);
        return ResponseEntity.ok (CustomApiResponse.success ("Connection type updated successfully", connectionTypeResponse, HttpStatus.OK.value ()));
    }

    @DeleteMapping("/types/{id}")
    @Operation(summary = "Delete Connection Type", description = "Delete a connection type by its ID")
    public ResponseEntity<CustomApiResponse<String>> deleteConnectionType (@PathVariable String id) {
        connectionTypeService.deleteConnectionType (id);
        return ResponseEntity.ok (CustomApiResponse.success ("Connection type deleted successfully", null, HttpStatus.OK.value ()));
    }

    @GetMapping("/types")
    @Operation(summary = "Search Connection Types with Pagination", description = "Search connection types with optional filters and pagination")
    public ResponseEntity<CustomApiResponse<PaginationResponse<ConnectionTypeResponse>>> searchConnectionTypes (
            @ModelAttribute PaginationRequest paginationRequest) {

        if (paginationRequest.getPage () == null) paginationRequest.setPage (0);
        if (paginationRequest.getSize () == null) paginationRequest.setSize (10);

        PaginationResponse<ConnectionTypeResponse> response = connectionTypeService.searchConnectionTypesWithPagination (paginationRequest);

        return ResponseEntity.ok (CustomApiResponse.success ("Connection types fetched successfully", response, HttpStatus.OK.value ()));
    }

    // ------------------------ Tariff Management ------------------------

    @PostMapping("/tariffs")
    @Operation(summary = "Create Tariff", description = "Create a new tariff")
    public ResponseEntity<CustomApiResponse<TariffResponse>> createTariff (
            @RequestBody @Valid TariffRequest tariffRequest) {
        TariffResponse tariffResponse = tariffService.createTariff (tariffRequest);
        return ResponseEntity.status (HttpStatus.CREATED)
                .body (CustomApiResponse.success ("Tariff created successfully", tariffResponse, HttpStatus.CREATED.value ()));
    }

    @GetMapping("/tariffs/{id}")
    @Operation(summary = "Get Tariff by ID", description = "Retrieve a tariff by its ID")
    public ResponseEntity<CustomApiResponse<TariffResponse>> getTariffById (@PathVariable String id) {
        TariffResponse tariffResponse = tariffService.getTariffById (id);
        return ResponseEntity.ok (CustomApiResponse.success ("Tariff fetched successfully", tariffResponse, HttpStatus.OK.value ()));
    }

    @PutMapping("/tariffs/{id}")
    @Operation(summary = "Update Tariff", description = "Update tariff details by ID")
    public ResponseEntity<CustomApiResponse<TariffResponse>> updateTariff (
            @PathVariable String id,
            @RequestBody @Valid TariffRequest tariffRequest) {
        TariffResponse tariffResponse = tariffService.updateTariff (id, tariffRequest);
        return ResponseEntity.ok (CustomApiResponse.success ("Tariff updated successfully", tariffResponse, HttpStatus.OK.value ()));
    }

    @DeleteMapping("/tariffs/{id}")
    @Operation(summary = "Delete Tariff", description = "Delete a tariff by its ID")
    public ResponseEntity<CustomApiResponse<String>> deleteTariff (@PathVariable String id) {
        tariffService.deleteTariff (id);
        return ResponseEntity.ok (CustomApiResponse.success ("Tariff deleted successfully", null, HttpStatus.OK.value ()));
    }

    // ------------------------ Invoice Management ------------------------

    @PostMapping("/invoices")  // Create a new invoice
    @Operation(summary = "Create Invoice", description = "Create a new invoice")
    public ResponseEntity<CustomApiResponse<InvoiceResponse>> createInvoice (
            @RequestBody @Valid InvoiceRequest invoiceRequest) {
        InvoiceResponse invoiceResponse = invoiceService.createInvoice (invoiceRequest);
        return ResponseEntity.status (HttpStatus.CREATED)
                .body (CustomApiResponse.success ("Invoice created successfully", invoiceResponse, HttpStatus.CREATED.value ()));
    }

    @GetMapping("/invoices/{id}")  // Retrieve invoice by ID
    @Operation(summary = "Get Invoice by ID", description = "Retrieve an invoice by its ID")
    public ResponseEntity<CustomApiResponse<InvoiceResponse>> getInvoiceById (@PathVariable String id) {
        InvoiceResponse invoiceResponse = invoiceService.getInvoiceById (id);
        return ResponseEntity.ok (CustomApiResponse.success ("Invoice fetched successfully", invoiceResponse, HttpStatus.OK.value ()));
    }

    @GetMapping("/invoices")  // Retrieve all invoices
    @Operation(summary = "Get Invoices", description = "Retrieve all invoices")
    public ResponseEntity<CustomApiResponse<List<InvoiceResponse>>> getInvoices (
            @RequestParam(required = false, defaultValue = "PENDING") InvoiceTransactionStatus paymentStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, // Optional filter for start date
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate // Optional filter for end date
    ) {
        Authentication authentication = SecurityContextHolder.getContext ().getAuthentication ();
        String customerId = authentication.getName ();

        List<InvoiceResponse> invoiceResponses = invoiceService.getAllInvoicesForCustomer (customerId, paymentStatus, startDate, endDate);

        // Sort the invoice responses in descending order by due date
        invoiceResponses.sort (Comparator.comparing (InvoiceResponse::getDueDate).reversed ());

        return ResponseEntity.ok (CustomApiResponse.success ("Invoices fetched successfully", invoiceResponses, HttpStatus.OK.value ()));
    }


    @PostMapping("/invoices/bulk")
    @Operation(summary = "Create Invoices", description = "Create multiple invoices")
    public ResponseEntity<CustomApiResponse<BulkInvoiceResponse>> createInvoices (
            @RequestBody @Valid BulkInvoiceRequest invoiceRequests) {
        BulkInvoiceResponse invoiceResponses = invoiceService.addBulkInvoices (invoiceRequests);
        return ResponseEntity.status (HttpStatus.CREATED)
                .body (CustomApiResponse.success ("Invoices created successfully", invoiceResponses, HttpStatus.CREATED.value ()));
    }

    @PostMapping("/invoices/bulk/csv")
    public ResponseEntity<CustomApiResponse<BulkInvoiceResponse>> uploadInvoicesCsv (@RequestParam("invoiceFile") MultipartFile file) {
        BulkInvoiceResponse response = invoiceService.addBulkInvoicesWithCsv (file);
        return ResponseEntity.status (HttpStatus.CREATED).body (CustomApiResponse.success ("Invoices created successfully", response, HttpStatus.CREATED.value ()));
    }

    @PostMapping("/invoices/payment/upi")
    @Operation(summary = "Pay Invoice using UPI", description = "Pay an invoice using UPI")
    public ResponseEntity<CustomApiResponse<Transaction>> payInvoiceUsingUpi (@RequestBody PayInvoiceByUpi payByUpi) {
        Transaction transaction = invoiceService.payByUpi (payByUpi);
        return ResponseEntity.ok (CustomApiResponse.success ("Invoice paid successfully", transaction, HttpStatus.OK.value ()));
    }


}
