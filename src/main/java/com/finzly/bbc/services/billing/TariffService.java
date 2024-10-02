package com.finzly.bbc.services.billing;

import com.finzly.bbc.dto.billing.TariffDTO;
import com.finzly.bbc.dto.billing.mapper.TariffMapper;
import com.finzly.bbc.models.billing.ConnectionType;
import com.finzly.bbc.models.billing.Tariff;
import com.finzly.bbc.repositories.billing.ConnectionTypeRepository;
import com.finzly.bbc.repositories.billing.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service for Tariff entity
@Service
@Transactional
public class TariffService {

    private final TariffRepository tariffRepository;
    private final TariffMapper tariffMapper;
    private final ConnectionTypeRepository connectionTypeRepository;

    @Autowired
    public TariffService (TariffRepository tariffRepository, TariffMapper tariffMapper,
                          ConnectionTypeRepository connectionTypeRepository) {
        this.tariffRepository = tariffRepository;
        this.tariffMapper = tariffMapper;
        this.connectionTypeRepository = connectionTypeRepository;
    }

    // CRUD Operations
    public List<TariffDTO> getAllTariffs () {
        return tariffRepository.findAll ().stream ()
                .map (tariffMapper::toTariffDTO)
                .collect (Collectors.toList ());
    }

    public Optional<TariffDTO> getTariffById (String id) {
        return tariffRepository.findById (id).map (tariffMapper::toTariffDTO);
    }

    public TariffDTO createTariff (TariffDTO tariffDTO) {
        ConnectionType connectionType = connectionTypeRepository.findById (tariffDTO.getConnectionTypeId ()).orElseThrow ();
        Tariff tariff = tariffMapper.toTariffEntity (tariffDTO, connectionType);
        return tariffMapper.toTariffDTO (tariffRepository.save (tariff));
    }

    public TariffDTO updateTariff (String id, TariffDTO tariffDTO) {
        Tariff tariff = tariffRepository.findById (id).orElseThrow ();
        tariffMapper.updateTariffEntity (tariff, tariffDTO);
        return tariffMapper.toTariffDTO (tariffRepository.save (tariff));
    }

    public void deleteTariff (String id) {
        tariffRepository.deleteById (id);
    }

    // Searching and Filtering
    public List<TariffDTO> findTariffsByConnectionTypeId (String connectionTypeId) {
        return tariffRepository.findByConnectionTypeId (connectionTypeId).stream ()
                .map (tariffMapper::toTariffDTO)
                .collect (Collectors.toList ());
    }
}
