package com.finzly.bbc.services.billing;

import com.finzly.bbc.dtos.billing.TariffSlabRequest;
import com.finzly.bbc.dtos.billing.TariffSlabResponse;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.models.billing.TariffSlab;
import com.finzly.bbc.repositories.billing.TariffSlabRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TariffSlabService {

    private final TariffSlabRepository tariffSlabRepository;
    private final ModelMapper modelMapper;

    public TariffSlabResponse createTariffSlab(TariffSlabRequest request) {
        TariffSlab tariffSlab = modelMapper.map(request, TariffSlab.class);
        TariffSlab savedTariffSlab = tariffSlabRepository.save(tariffSlab);
        return modelMapper.map(savedTariffSlab, TariffSlabResponse.class);
    }

    public TariffSlabResponse getTariffSlabById(String id) {
        TariffSlab tariffSlab = tariffSlabRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff slab not found with ID: " + id));
        return modelMapper.map(tariffSlab, TariffSlabResponse.class);
    }

    public TariffSlabResponse updateTariffSlab(String id, TariffSlabRequest request) {
        TariffSlab tariffSlab = tariffSlabRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff slab not found with ID: " + id));

        tariffSlab.setMinUnits(request.getMinUnits());
        tariffSlab.setMaxUnits(request.getMaxUnits());
        tariffSlab.setRatePerUnit(request.getRatePerUnit());

        TariffSlab updatedTariffSlab = tariffSlabRepository.save(tariffSlab);
        return modelMapper.map(updatedTariffSlab, TariffSlabResponse.class);
    }

    public void deleteTariffSlab(String id) {
        TariffSlab tariffSlab = tariffSlabRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff slab not found with ID: " + id));
        tariffSlabRepository.delete(tariffSlab);
    }
}
