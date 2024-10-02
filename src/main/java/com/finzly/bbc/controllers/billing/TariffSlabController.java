package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dto.billing.TariffSlabDTO;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.billing.TariffSlabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariff-slabs")
public class TariffSlabController {

    private final TariffSlabService tariffSlabService;

    @Autowired
    public TariffSlabController (TariffSlabService tariffSlabService) {
        this.tariffSlabService = tariffSlabService;
    }

    // Get all tariff slabs
    @GetMapping
    public ApiResponse<List<TariffSlabDTO>> getAllTariffSlabs () {
        List<TariffSlabDTO> tariffSlabs = tariffSlabService.getAllTariffSlabs ();
        return ApiResponse.success ("Fetched all tariff slabs successfully", tariffSlabs);
    }

    // Get tariff slab by ID
    @GetMapping("/{id}")
    public ApiResponse<TariffSlabDTO> getTariffSlabById (@PathVariable String id) {
        return tariffSlabService.getTariffSlabById (id)
                .map (tariffSlabDTO -> ApiResponse.success ("Fetched tariff slab successfully", tariffSlabDTO))
                .orElseGet (() -> ApiResponse.error ("Tariff slab not found", 404));
    }

    // Create a new tariff slab
    @PostMapping
    public ApiResponse<TariffSlabDTO> createTariffSlab (@RequestBody TariffSlabDTO tariffSlabDTO) {
        TariffSlabDTO createdTariffSlab = tariffSlabService.createTariffSlab (tariffSlabDTO);
        return ApiResponse.success ("Created tariff slab successfully", createdTariffSlab);
    }

    // Update an existing tariff slab
    @PutMapping("/{id}")
    public ApiResponse<TariffSlabDTO> updateTariffSlab (@PathVariable String id, @RequestBody TariffSlabDTO tariffSlabDTO) {
        TariffSlabDTO updatedTariffSlab = tariffSlabService.updateTariffSlab (id, tariffSlabDTO);
        return ApiResponse.success ("Updated tariff slab successfully", updatedTariffSlab);
    }

    // Delete a tariff slab
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTariffSlab (@PathVariable String id) {
        tariffSlabService.deleteTariffSlab (id);
        return ApiResponse.success ("Deleted tariff slab successfully", null);
    }

    // Get tariff slabs by tariff ID
    @GetMapping("/tariff/{tariffId}")
    public ApiResponse<List<TariffSlabDTO>> findTariffSlabsByTariffId (@PathVariable String tariffId) {
        List<TariffSlabDTO> tariffSlabs = tariffSlabService.findTariffSlabsByTariffId (tariffId);
        return ApiResponse.success ("Fetched tariff slabs by tariff successfully", tariffSlabs);
    }
}
