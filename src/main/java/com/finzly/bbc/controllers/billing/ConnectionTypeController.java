package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dto.billing.ConnectionTypeDTO;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.billing.ConnectionTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connection-types")
@Tag(name = "Connection Type API", description = "Connection Type API for connection type management, create, update, delete, get, get all")
public class ConnectionTypeController {

    private final ConnectionTypeService connectionTypeService;

    @Autowired
    public ConnectionTypeController (ConnectionTypeService connectionTypeService) {
        this.connectionTypeService = connectionTypeService;
    }

    // Get all connection types
    @GetMapping
    @Operation(summary = "Get all connection types")
    public ApiResponse<List<ConnectionTypeDTO>> getAllConnectionTypes () {
        List<ConnectionTypeDTO> connectionTypes = connectionTypeService.getAllConnectionTypes ();
        return ApiResponse.success ("Fetched all connection types successfully", connectionTypes);
    }

    // Get connection type by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get connection type by ID")
    public ApiResponse<ConnectionTypeDTO> getConnectionTypeById (@PathVariable String id) {
        return connectionTypeService.getConnectionTypeById (id)
                .map (connectionTypeDTO -> ApiResponse.success ("Connection type found", connectionTypeDTO))
                .orElse (ApiResponse.error ("Connection type not found", 404));
    }

    // Create a new connection type
    @PostMapping
    @Operation(summary = "Create a new connection type")
    public ApiResponse<ConnectionTypeDTO> createConnectionType (@RequestBody ConnectionTypeDTO connectionTypeDTO) {
        ConnectionTypeDTO createdConnectionType = connectionTypeService.createConnectionType (connectionTypeDTO);
        return ApiResponse.success ("Connection type created successfully", createdConnectionType);
    }

    // Update an existing connection type
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing connection type")
    public ApiResponse<ConnectionTypeDTO> updateConnectionType (@PathVariable String id, @RequestBody ConnectionTypeDTO connectionTypeDTO) {
        ConnectionTypeDTO updatedConnectionType = connectionTypeService.updateConnectionType (id, connectionTypeDTO);
        return ApiResponse.success ("Connection type updated successfully", updatedConnectionType);
    }

    // Delete a connection type
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a connection type")
    public ApiResponse<Void> deleteConnectionType (@PathVariable String id) {
        connectionTypeService.deleteConnectionType (id);
        return ApiResponse.success ("Connection type deleted successfully", null);
    }
}
