package com.finzly.bbc.controllers.billing;

import com.finzly.bbc.dto.billing.TariffDTO;
import com.finzly.bbc.response.ApiResponse;
import com.finzly.bbc.services.billing.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;

    @Autowired
    public TariffController (TariffService tariffService) {
        this.tariffService = tariffService;
    }

    // Get all tariffs
    @GetMapping
    public ApiResponse<List<TariffDTO>> getAllTariffs () {
        List<TariffDTO> tariffs = tariffService.getAllTariffs ();
        return ApiResponse.success ("Fetched all tariffs successfully", tariffs);
    }

    // Get tariff by ID
    @GetMapping("/{id}")
    public ApiResponse<TariffDTO> getTariffById (@PathVariable String id) {
        return tariffService.getTariffById (id)
                .map (tariffDTO -> ApiResponse.success ("Fetched tariff successfully", tariffDTO))
                .orElseGet (() -> ApiResponse.error ("Tariff not found", 404));
    }

    // Create a new tariff
    @PostMapping
    public ApiResponse<TariffDTO> createTariff (@RequestBody TariffDTO tariffDTO) {
        TariffDTO createdTariff = tariffService.createTariff (tariffDTO);
        return ApiResponse.success ("Created tariff successfully", createdTariff);
    }

    // Update an existing tariff
    @PutMapping("/{id}")
    public ApiResponse<TariffDTO> updateTariff (@PathVariable String id, @RequestBody TariffDTO tariffDTO) {
        TariffDTO updatedTariff = tariffService.updateTariff (id, tariffDTO);
        return ApiResponse.success ("Updated tariff successfully", updatedTariff);
    }

    // Delete a tariff
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTariff (@PathVariable String id) {
        tariffService.deleteTariff (id);
        return ApiResponse.success ("Deleted tariff successfully", null);
    }

    // Get tariffs by connection type ID
    @GetMapping("/connection-type/{connectionTypeId}")
    public ApiResponse<List<TariffDTO>> findTariffsByConnectionTypeId (@PathVariable String connectionTypeId) {
        List<TariffDTO> tariffs = tariffService.findTariffsByConnectionTypeId (connectionTypeId);
        return ApiResponse.success ("Fetched tariffs by connection type successfully", tariffs);
    }
}


