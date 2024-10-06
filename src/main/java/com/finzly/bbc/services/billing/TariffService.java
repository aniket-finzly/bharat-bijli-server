package com.finzly.bbc.services.billing;

import com.finzly.bbc.dtos.billing.TariffRequest;
import com.finzly.bbc.dtos.billing.TariffResponse;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.models.billing.ConnectionType;
import com.finzly.bbc.models.billing.Tariff;
import com.finzly.bbc.repositories.billing.ConnectionTypeRepository;
import com.finzly.bbc.repositories.billing.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepository tariffRepository;
    private final ConnectionTypeRepository connectionTypeRepository;
    private final ModelMapper modelMapper;

    public TariffResponse createTariff(TariffRequest request) {
        Tariff tariff = modelMapper.map(request, Tariff.class);
        Tariff savedTariff = tariffRepository.save(tariff);
        return modelMapper.map(savedTariff, TariffResponse.class);
    }

    public TariffResponse getTariffById(String id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff not found with ID: " + id));
        return modelMapper.map(tariff, TariffResponse.class);
    }

    public TariffResponse updateTariff(String id, TariffRequest request) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff not found with ID: " + id));


        ConnectionType connectionType = connectionTypeRepository.findById(request.getConnectionTypeId()).orElseThrow (() -> new ResourceNotFoundException("Connection Type not found with ID: " + request.getConnectionTypeId()));

        tariff.setDescription(request.getDescription());
        tariff.setConnectionType(connectionType);

        Tariff updatedTariff = tariffRepository.save(tariff);
        return modelMapper.map(updatedTariff, TariffResponse.class);
    }

    public void deleteTariff(String id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tariff not found with ID: " + id));
        tariffRepository.delete(tariff);
    }

}
