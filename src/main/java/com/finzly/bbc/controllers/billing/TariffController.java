package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dto.billing.TariffDTO;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.billing.TariffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@Tag(name = "Tariff API", description = "API for managing tariffs: create, update, delete, retrieve tariffs.")
public class TariffController {

    private final TariffService tariffService;

    @Autowired
    public TariffController (TariffService tariffService) {
        this.tariffService = tariffService;
    }

    // Get all tariffs
    @GetMapping
    @Operation(summary = "Get all tariffs",
            description = "Retrieves a list of all tariffs.")
    public ApiResponse<List<TariffDTO>> getAllTariffs () {
        List<TariffDTO> tariffs = tariffService.getAllTariffs ();
        return ApiResponse.success ("Fetched all tariffs successfully", tariffs);
    }

    // Get tariff by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get tariff by ID",
            description = "Retrieves a specific tariff by its ID.")
    public ApiResponse<TariffDTO> getTariffById (@PathVariable String id) {
        return tariffService.getTariffById (id)
                .map (tariffDTO -> ApiResponse.success ("Fetched tariff successfully", tariffDTO))
                .orElseGet (() -> ApiResponse.error ("Tariff not found", 404));
    }

    // Create a new tariff
    @PostMapping
    @Operation(summary = "Create a new tariff",
            description = "Creates a new tariff with the provided details.")
    public ApiResponse<TariffDTO> createTariff (@RequestBody TariffDTO tariffDTO) {
        TariffDTO createdTariff = tariffService.createTariff (tariffDTO);
        return ApiResponse.success ("Created tariff successfully", createdTariff);
    }

    // Update an existing tariff
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing tariff",
            description = "Updates the details of an existing tariff.")
    public ApiResponse<TariffDTO> updateTariff (@PathVariable String id, @RequestBody TariffDTO tariffDTO) {
        TariffDTO updatedTariff = tariffService.updateTariff (id, tariffDTO);
        return ApiResponse.success ("Updated tariff successfully", updatedTariff);
    }

    // Delete a tariff
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tariff",
            description = "Deletes the tariff identified by the specified ID.")
    public ApiResponse<Void> deleteTariff (@PathVariable String id) {
        tariffService.deleteTariff (id);
        return ApiResponse.success ("Deleted tariff successfully", null);
    }

    // Get tariffs by connection type ID
    @GetMapping("/connection-type/{connectionTypeId}")
    @Operation(summary = "Get tariffs by connection type ID",
            description = "Retrieves a list of tariffs associated with a specific connection type ID.")
    public ApiResponse<List<TariffDTO>> findTariffsByConnectionTypeId (@PathVariable String connectionTypeId) {
        List<TariffDTO> tariffs = tariffService.findTariffsByConnectionTypeId (connectionTypeId);
        return ApiResponse.success ("Fetched tariffs by connection type successfully", tariffs);
    }
}
