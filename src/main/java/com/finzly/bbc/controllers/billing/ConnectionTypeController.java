package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dto.billing.ConnectionTypeDTO;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.billing.ConnectionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connection-types")
public class ConnectionTypeController {

    private final ConnectionTypeService connectionTypeService;

    @Autowired
    public ConnectionTypeController (ConnectionTypeService connectionTypeService) {
        this.connectionTypeService = connectionTypeService;
    }

    // Get all connection types
    @GetMapping
    public ApiResponse<List<ConnectionTypeDTO>> getAllConnectionTypes () {
        List<ConnectionTypeDTO> connectionTypes = connectionTypeService.getAllConnectionTypes ();
        return ApiResponse.success ("Fetched all connection types successfully", connectionTypes);
    }

    // Get connection type by ID
    @GetMapping("/{id}")
    public ApiResponse<ConnectionTypeDTO> getConnectionTypeById (@PathVariable String id) {
        return connectionTypeService.getConnectionTypeById (id)
                .map (connectionTypeDTO -> ApiResponse.success ("Connection type found", connectionTypeDTO))
                .orElse (ApiResponse.error ("Connection type not found", 404));
    }

    // Create a new connection type
    @PostMapping
    public ApiResponse<ConnectionTypeDTO> createConnectionType (@RequestBody ConnectionTypeDTO connectionTypeDTO) {
        ConnectionTypeDTO createdConnectionType = connectionTypeService.createConnectionType (connectionTypeDTO);
        return ApiResponse.success ("Connection type created successfully", createdConnectionType);
    }

    // Update an existing connection type
    @PutMapping("/{id}")
    public ApiResponse<ConnectionTypeDTO> updateConnectionType (@PathVariable String id, @RequestBody ConnectionTypeDTO connectionTypeDTO) {
        ConnectionTypeDTO updatedConnectionType = connectionTypeService.updateConnectionType (id, connectionTypeDTO);
        return ApiResponse.success ("Connection type updated successfully", updatedConnectionType);
    }

    // Delete a connection type
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteConnectionType (@PathVariable String id) {
        connectionTypeService.deleteConnectionType (id);
        return ApiResponse.success ("Connection type deleted successfully", null);
    }
}



