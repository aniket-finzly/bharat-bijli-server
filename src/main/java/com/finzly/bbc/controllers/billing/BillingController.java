package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dtos.billing.*;
import com.finzly.bbc.dtos.common.PaginationRequest;
import com.finzly.bbc.dtos.common.PaginationResponse;
import com.finzly.bbc.response.CustomApiResponse;
import com.finzly.bbc.services.billing.ConnectionService;
import com.finzly.bbc.services.billing.ConnectionTypeService;
import com.finzly.bbc.services.billing.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/billing/connections")
@RequiredArgsConstructor
@Tag(name = "Connection API", description = "API for managing connections with CRUD operations and pagination")
public class BillingController {

    private final ConnectionService connectionService;
    private final ConnectionTypeService connectionTypeService;
    private final InvoiceService invoiceService;

    // ------------------------ Connection Management ------------------------

    @PostMapping
    @Operation(summary = "Create Connection", description = "Create a new connection")
    public ResponseEntity<CustomApiResponse<ConnectionResponse>> createConnection(
            @RequestBody @Valid ConnectionRequest connectionRequest) {
        ConnectionResponse connectionResponse = connectionService.createConnection(connectionRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomApiResponse.success("Connection created successfully", connectionResponse, HttpStatus.CREATED.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Connection by ID", description = "Retrieve a connection by its ID")
    public ResponseEntity<CustomApiResponse<ConnectionResponse>> getConnectionById(@PathVariable String id) {
        ConnectionResponse connectionResponse = connectionService.getConnectionById(id);
        return ResponseEntity.ok(CustomApiResponse.success("Connection fetched successfully", connectionResponse, HttpStatus.OK.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Connection", description = "Update connection details by ID")
    public ResponseEntity<CustomApiResponse<ConnectionResponse>> updateConnection(
            @PathVariable String id,
            @RequestBody @Valid ConnectionRequest connectionRequest) {
        ConnectionResponse connectionResponse = connectionService.updateConnection(id, connectionRequest);
        return ResponseEntity.ok(CustomApiResponse.success("Connection updated successfully", connectionResponse, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Connection", description = "Delete a connection by its ID")
    public ResponseEntity<CustomApiResponse<String>> deleteConnection(@PathVariable String id) {
        connectionService.deleteConnection(id);
        return ResponseEntity.ok(CustomApiResponse.success("Connection deleted successfully", null, HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Search Connections with Pagination", description = "Search connections with optional filters and pagination")
    public ResponseEntity<CustomApiResponse<PaginationResponse<ConnectionResponse>>> searchConnections(
            @ModelAttribute PaginationRequest paginationRequest,
            @RequestParam(value = "customerId", required = false) String customerId,
            @RequestParam(value = "connectionTypeId", required = false) String connectionTypeId,
            @RequestParam(value = "status", required = false) String status) {

        if (paginationRequest.getPage() == null) paginationRequest.setPage(0);
        if (paginationRequest.getSize() == null) paginationRequest.setSize(10);

        PaginationResponse<ConnectionResponse> response = connectionService.searchConnectionsWithPagination(
                customerId, connectionTypeId, status, paginationRequest
        );

        return ResponseEntity.ok(CustomApiResponse.success("Connections fetched successfully", response, HttpStatus.OK.value()));
    }
    // ------------------------ Connection Type Management ------------------------

    @PostMapping("/types")
    @Operation(summary = "Create Connection Type", description = "Create a new connection type")
    public ResponseEntity<CustomApiResponse<ConnectionTypeResponse>> createConnectionType(
            @RequestBody @Valid ConnectionTypeRequest connectionTypeRequest) {
        ConnectionTypeResponse connectionTypeResponse = connectionTypeService.createConnectionType(connectionTypeRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomApiResponse.success("Connection type created successfully", connectionTypeResponse, HttpStatus.CREATED.value()));
    }

    @GetMapping("/types/{id}")
    @Operation(summary = "Get Connection Type by ID", description = "Retrieve a connection type by its ID")
    public ResponseEntity<CustomApiResponse<ConnectionTypeResponse>> getConnectionTypeById(@PathVariable String id) {
        ConnectionTypeResponse connectionTypeResponse = connectionTypeService.getConnectionTypeById(id);
        return ResponseEntity.ok(CustomApiResponse.success("Connection type fetched successfully", connectionTypeResponse, HttpStatus.OK.value()));
    }

    @PutMapping("/types/{id}")
    @Operation(summary = "Update Connection Type", description = "Update connection type details by ID")
    public ResponseEntity<CustomApiResponse<ConnectionTypeResponse>> updateConnectionType(
            @PathVariable String id,
            @RequestBody @Valid ConnectionTypeRequest connectionTypeRequest) {
        ConnectionTypeResponse connectionTypeResponse = connectionTypeService.updateConnectionType(id, connectionTypeRequest);
        return ResponseEntity.ok(CustomApiResponse.success("Connection type updated successfully", connectionTypeResponse, HttpStatus.OK.value()));
    }

    @DeleteMapping("/types/{id}")
    @Operation(summary = "Delete Connection Type", description = "Delete a connection type by its ID")
    public ResponseEntity<CustomApiResponse<String>> deleteConnectionType(@PathVariable String id) {
        connectionTypeService.deleteConnectionType(id);
        return ResponseEntity.ok(CustomApiResponse.success("Connection type deleted successfully", null, HttpStatus.OK.value()));
    }

    @GetMapping("/types")
    @Operation(summary = "Search Connection Types with Pagination", description = "Search connection types with optional filters and pagination")
    public ResponseEntity<CustomApiResponse<PaginationResponse<ConnectionTypeResponse>>> searchConnectionTypes(
            @ModelAttribute PaginationRequest paginationRequest) {

        if (paginationRequest.getPage() == null) paginationRequest.setPage(0);
        if (paginationRequest.getSize() == null) paginationRequest.setSize(10);

        PaginationResponse<ConnectionTypeResponse> response = connectionTypeService.searchConnectionTypesWithPagination(paginationRequest);

        return ResponseEntity.ok(CustomApiResponse.success("Connection types fetched successfully", response, HttpStatus.OK.value()));
    }

    // ------------------------ Invoice Management ------------------------

    @PostMapping("/invoices")  // Create a new invoice at a distinct endpoint
    @Operation(summary = "Create Invoice", description = "Create a new invoice")
    public ResponseEntity<CustomApiResponse<InvoiceResponse>> createInvoice(
            @RequestBody @Valid InvoiceRequest invoiceRequest) {
        InvoiceResponse invoiceResponse = invoiceService.createInvoice(invoiceRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomApiResponse.success("Invoice created successfully", invoiceResponse, HttpStatus.CREATED.value()));
    }

    @GetMapping("/invoices/{id}")  // Retrieve invoice by ID
    @Operation(summary = "Get Invoice by ID", description = "Retrieve an invoice by its ID")
    public ResponseEntity<CustomApiResponse<InvoiceResponse>> getInvoiceById(@PathVariable String id) {
        InvoiceResponse invoiceResponse = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(CustomApiResponse.success("Invoice fetched successfully", invoiceResponse, HttpStatus.OK.value()));
    }

    @PutMapping("/invoices/{id}")  // Update an invoice by its ID
    @Operation(summary = "Update Invoice", description = "Update an invoice by its ID")
    public ResponseEntity<CustomApiResponse<InvoiceResponse>> updateInvoice (@PathVariable String id, @RequestBody @Valid InvoiceRequest invoiceRequest) {
        InvoiceResponse invoiceResponse = invoiceService.updateInvoice(id, invoiceRequest);
        return ResponseEntity.ok(CustomApiResponse.success("Invoice updated successfully", invoiceResponse, HttpStatus.OK.value()));
    }

    @DeleteMapping("/invoices/{id}") // Delete an invoice by its ID
    @Operation(summary = "Delete Invoice", description = "Delete an invoice by its ID")
    public ResponseEntity<CustomApiResponse<String>> deleteInvoice(@PathVariable String id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok(CustomApiResponse.success("Invoice deleted successfully", null, HttpStatus.OK.value()));
    }

    @GetMapping("/invoices")
    @Operation(summary = "Search Invoices with Pagination", description = "Search invoices with optional filters and pagination")
    public ResponseEntity<CustomApiResponse<PaginationResponse<InvoiceResponse>>> searchInvoices(
            @ModelAttribute PaginationRequest paginationRequest,
            @RequestParam(value = "connectionId", required = false) String connectionId,
            @RequestParam(value = "dueDate", required = false) String dueDate) {

        if (paginationRequest.getPage() == null) paginationRequest.setPage(0);
        if (paginationRequest.getSize() == null) paginationRequest.setSize(10);

        PaginationResponse<InvoiceResponse> response = invoiceService.searchInvoicesWithPagination (connectionId, LocalDateTime.parse (dueDate), paginationRequest);
        return ResponseEntity.ok(CustomApiResponse.success("Invoices fetched successfully", response, HttpStatus.OK.value()));
    }
}
