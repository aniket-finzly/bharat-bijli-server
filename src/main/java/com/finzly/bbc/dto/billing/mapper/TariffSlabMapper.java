package com.finzly.bbc.dto.billing.mapper;

import com.finzly.bbc.dto.billing.TariffSlabDTO;
import com.finzly.bbc.models.billing.Tariff;
import com.finzly.bbc.models.billing.TariffSlab;
import org.springframework.stereotype.Component;

// Mapper for TariffSlab entity
@Component
public class TariffSlabMapper {

    public TariffSlabDTO toTariffSlabDTO (TariffSlab tariffSlab) {
        return new TariffSlabDTO (
                tariffSlab.getId (),
                tariffSlab.getMinUnits (),
                tariffSlab.getMaxUnits (),
                tariffSlab.getRatePerUnit (),
                tariffSlab.getTariff ().getId ()
        );
    }

    public TariffSlab toTariffSlabEntity (TariffSlabDTO tariffSlabDTO, Tariff tariff) {
        return new TariffSlab (
                tariffSlabDTO.getId (),
                tariffSlabDTO.getMinUnits (),
                tariffSlabDTO.getMaxUnits (),
                tariffSlabDTO.getRatePerUnit (),
                tariff
        );
    }

    public void updateTariffSlabEntity (TariffSlab tariffSlab, TariffSlabDTO tariffSlabDTO) {
        // Update the existing TariffSlab entity with new values
        tariffSlab.setMinUnits (tariffSlabDTO.getMinUnits ());
        tariffSlab.setMaxUnits (tariffSlabDTO.getMaxUnits ());
        tariffSlab.setRatePerUnit (tariffSlabDTO.getRatePerUnit ());
        // Note: The tariff is not updated in this method, as it is usually handled separately.
    }
}
