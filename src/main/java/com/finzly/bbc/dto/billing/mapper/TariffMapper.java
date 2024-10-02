package com.finzly.bbc.dto.billing.mapper;

import com.finzly.bbc.dto.billing.TariffDTO;
import com.finzly.bbc.models.billing.ConnectionType;
import com.finzly.bbc.models.billing.Tariff;
import org.springframework.stereotype.Component;

// Mapper for Tariff entity
@Component
public class TariffMapper {

    public TariffDTO toTariffDTO (Tariff tariff) {
        return new TariffDTO (
                tariff.getId (),
                tariff.getConnectionType ().getId (),
                tariff.getDescription ()
        );
    }

    public Tariff toTariffEntity (TariffDTO tariffDTO, ConnectionType connectionType) {
        return new Tariff (
                tariffDTO.getId (),
                connectionType,
                null, // assuming slabs list will be set elsewhere
                tariffDTO.getDescription ()
        );
    }

    public void updateTariffEntity (Tariff tariff, TariffDTO tariffDTO) {
        // Update the existing Tariff entity with new values
        tariff.setDescription (tariffDTO.getDescription ());
        // Note: The connectionType is not updated in this method, as it is usually handled separately.
    }
}
