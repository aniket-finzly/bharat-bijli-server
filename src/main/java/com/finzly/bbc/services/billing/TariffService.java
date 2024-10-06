package com.finzly.bbc.services.billing;

import com.finzly.bbc.dtos.billing.TariffRequest;
import com.finzly.bbc.dtos.billing.TariffResponse;
import com.finzly.bbc.exceptions.BadRequestException;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.models.billing.ConnectionType;
import com.finzly.bbc.models.billing.Tariff;
import com.finzly.bbc.repositories.billing.ConnectionTypeRepository;
import com.finzly.bbc.repositories.billing.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private ConnectionTypeRepository connectionTypeRepository;

    public TariffResponse createTariff (TariffRequest tariffRequest) {
        ConnectionType connectionType = connectionTypeRepository.findById (tariffRequest.getConnectionTypeId ())
                .orElseThrow (() -> new ResourceNotFoundException ("ConnectionType not found"));

        // Check if the range overlaps with any existing tariffs for this connection type
        validateTariffRange (connectionType, tariffRequest.getMinUnits (), tariffRequest.getMaxUnits (), null);

        Tariff tariff = new Tariff ();
        tariff.setConnectionType (connectionType);
        tariff.setMinUnits (tariffRequest.getMinUnits ());
        tariff.setMaxUnits (tariffRequest.getMaxUnits ());
        tariff.setRatePerUnit (tariffRequest.getRatePerUnit ());
        Tariff savedTariff = tariffRepository.save (tariff);
        return mapToResponse (savedTariff);
    }

    public TariffResponse updateTariff (String id, TariffRequest tariffRequest) {
        Tariff tariff = tariffRepository.findById (id)
                .orElseThrow (() -> new ResourceNotFoundException ("Tariff not found"));

        ConnectionType connectionType = connectionTypeRepository.findById (tariffRequest.getConnectionTypeId ())
                .orElseThrow (() -> new ResourceNotFoundException ("ConnectionType not found"));

        // Check if the new range overlaps with any other existing tariffs for this connection type
        validateTariffRange (connectionType, tariffRequest.getMinUnits (), tariffRequest.getMaxUnits (), id);

        tariff.setConnectionType (connectionType);
        tariff.setMinUnits (tariffRequest.getMinUnits ());
        tariff.setMaxUnits (tariffRequest.getMaxUnits ());
        tariff.setRatePerUnit (tariffRequest.getRatePerUnit ());

        Tariff updatedTariff = tariffRepository.save (tariff);

        return mapToResponse (updatedTariff);
    }

    public TariffResponse getTariffById (String id) {
        Tariff tariff = tariffRepository.findById (id)
                .orElseThrow (() -> new ResourceNotFoundException ("Tariff not found"));
        return mapToResponse (tariff);
    }

    public List<TariffResponse> getAllTariffs () {
        List<Tariff> tariffs = tariffRepository.findAll ();
        return tariffs.stream ().map (this::mapToResponse).collect (Collectors.toList ());
    }

    public void deleteTariff (String id) {
        Tariff tariff = tariffRepository.findById (id)
                .orElseThrow (() -> new ResourceNotFoundException ("Tariff not found"));
        tariffRepository.delete (tariff);
    }

    // Validate that the tariff range does not overlap with existing ranges for the same connectionType
    private void validateTariffRange (ConnectionType connectionType, int minUnits, int maxUnits, String tariffId) {
        List<Tariff> existingTariffs = tariffRepository.findByConnectionType (connectionType);

        for (Tariff existingTariff : existingTariffs) {
            if (!existingTariff.getId ().equals (tariffId)) {  // Exclude the current tariff if updating
                if (rangesOverlap (minUnits, maxUnits, existingTariff.getMinUnits (), existingTariff.getMaxUnits ())) {
                    throw new BadRequestException ("Tariff range overlaps with an existing range.");
                }
            }
        }
    }

    // Check if two ranges overlap
    private boolean rangesOverlap (int min1, int max1, int min2, int max2) {
        return (min1 <= max2 && max1 >= min2);
    }

    private TariffResponse mapToResponse (Tariff tariff) {
        return new TariffResponse (
                tariff.getId (),
                tariff.getConnectionType ().getId (),
                tariff.getMinUnits (),
                tariff.getMaxUnits (),
                tariff.getRatePerUnit ()
        );
    }
}
