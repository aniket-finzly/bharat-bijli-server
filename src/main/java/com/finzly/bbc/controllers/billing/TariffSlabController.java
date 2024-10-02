package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dto.billing.TariffSlabDTO;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.billing.TariffSlabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariff-slabs")
@Tag(name = "Tariff Slab API", description = "API for managing tariff slabs: create, update, delete, retrieve tariff slabs.")
public class TariffSlabController {

    private final TariffSlabService tariffSlabService;

    @Autowired
    public TariffSlabController (TariffSlabService tariffSlabService) {
        this.tariffSlabService = tariffSlabService;
    }

    // Get all tariff slabs
    @GetMapping
    @Operation(summary = "Get all tariff slabs",
            description = "Retrieves a list of all tariff slabs.")
    public ApiResponse<List<TariffSlabDTO>> getAllTariffSlabs () {
        List<TariffSlabDTO> tariffSlabs = tariffSlabService.getAllTariffSlabs ();
        return ApiResponse.success ("Fetched all tariff slabs successfully", tariffSlabs);
    }

    // Get tariff slab by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get tariff slab by ID",
            description = "Retrieves a specific tariff slab by its ID.")
    public ApiResponse<TariffSlabDTO> getTariffSlabById (@PathVariable String id) {
        return tariffSlabService.getTariffSlabById (id)
                .map (tariffSlabDTO -> ApiResponse.success ("Fetched tariff slab successfully", tariffSlabDTO))
                .orElseGet (() -> ApiResponse.error ("Tariff slab not found", 404));
    }

    // Create a new tariff slab
    @PostMapping
    @Operation(summary = "Create a new tariff slab",
            description = "Creates a new tariff slab with the provided details.")
    public ApiResponse<TariffSlabDTO> createTariffSlab (@RequestBody TariffSlabDTO tariffSlabDTO) {
        TariffSlabDTO createdTariffSlab = tariffSlabService.createTariffSlab (tariffSlabDTO);
        return ApiResponse.success ("Created tariff slab successfully", createdTariffSlab);
    }

    // Update an existing tariff slab
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing tariff slab",
            description = "Updates the details of an existing tariff slab.")
    public ApiResponse<TariffSlabDTO> updateTariffSlab (@PathVariable String id, @RequestBody TariffSlabDTO tariffSlabDTO) {
        TariffSlabDTO updatedTariffSlab = tariffSlabService.updateTariffSlab (id, tariffSlabDTO);
        return ApiResponse.success ("Updated tariff slab successfully", updatedTariffSlab);
    }

    // Delete a tariff slab
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tariff slab",
            description = "Deletes the tariff slab identified by the specified ID.")
    public ApiResponse<Void> deleteTariffSlab (@PathVariable String id) {
        tariffSlabService.deleteTariffSlab (id);
        return ApiResponse.success ("Deleted tariff slab successfully", null);
    }

    // Get tariff slabs by tariff ID
    @GetMapping("/tariff/{tariffId}")
    @Operation(summary = "Get tariff slabs by tariff ID",
            description = "Retrieves a list of tariff slabs associated with a specific tariff ID.")
    public ApiResponse<List<TariffSlabDTO>> findTariffSlabsByTariffId (@PathVariable String tariffId) {
        List<TariffSlabDTO> tariffSlabs = tariffSlabService.findTariffSlabsByTariffId (tariffId);
        return ApiResponse.success ("Fetched tariff slabs by tariff successfully", tariffSlabs);
    }
}
