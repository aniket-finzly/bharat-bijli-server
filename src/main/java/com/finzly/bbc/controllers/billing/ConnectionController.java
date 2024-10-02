package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dto.billing.ConnectionDTO;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.billing.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    @Autowired
    public ConnectionController (ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    // Get all connections
    @GetMapping
    public ApiResponse<List<ConnectionDTO>> getAllConnections () {
        List<ConnectionDTO> connections = connectionService.getAllConnections ();
        return ApiResponse.success ("Fetched all connections successfully", connections);
    }

    // Get connection by ID
    @GetMapping("/{id}")
    public ApiResponse<ConnectionDTO> getConnectionById (@PathVariable String id) {
        return connectionService.getConnectionById (id)
                .map (connectionDTO -> ApiResponse.success ("Connection found", connectionDTO))
                .orElse (ApiResponse.error ("Connection not found", 404));
    }

    // Create a new connection
    @PostMapping
    public ApiResponse<ConnectionDTO> createConnection (@RequestBody ConnectionDTO connectionDTO) {
        ConnectionDTO createdConnection = connectionService.createConnection (connectionDTO);
        return ApiResponse.success ("Connection created successfully", createdConnection);
    }

    // Update an existing connection
    @PutMapping("/{id}")
    public ApiResponse<ConnectionDTO> updateConnection (@PathVariable String id, @RequestBody ConnectionDTO connectionDTO) {
        ConnectionDTO updatedConnection = connectionService.updateConnection (id, connectionDTO);
        return ApiResponse.success ("Connection updated successfully", updatedConnection);
    }

    // Delete a connection
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteConnection (@PathVariable String id) {
        connectionService.deleteConnection (id);
        return ApiResponse.success ("Connection deleted successfully", null);
    }

    // Search connections by customer ID
    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<ConnectionDTO>> searchConnectionsByCustomer (@PathVariable String customerId) {
        List<ConnectionDTO> connections = connectionService.searchConnectionsByCustomer (customerId);
        return ApiResponse.success ("Connections for customer fetched successfully", connections);
    }

    // Find active connections
    @GetMapping("/active")
    public ApiResponse<List<ConnectionDTO>> findActiveConnections () {
        List<ConnectionDTO> activeConnections = connectionService.findActiveConnections ();
        return ApiResponse.success ("Active connections fetched successfully", activeConnections);
    }
}



