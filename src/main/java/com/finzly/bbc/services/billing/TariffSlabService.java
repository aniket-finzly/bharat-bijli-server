package com.finzly.bbc.services.billing;

import com.finzly.bbc.dto.billing.TariffSlabDTO;
import com.finzly.bbc.dto.billing.mapper.TariffSlabMapper;
import com.finzly.bbc.models.billing.Tariff;
import com.finzly.bbc.models.billing.TariffSlab;
import com.finzly.bbc.repositories.billing.TariffRepository;
import com.finzly.bbc.repositories.billing.TariffSlabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service for TariffSlab entity
@Service
@Transactional
public class TariffSlabService {

    private final TariffSlabRepository tariffSlabRepository;
    private final TariffSlabMapper tariffSlabMapper;
    private final TariffRepository tariffRepository;

    @Autowired
    public TariffSlabService (TariffSlabRepository tariffSlabRepository, TariffSlabMapper tariffSlabMapper,
                              TariffRepository tariffRepository) {
        this.tariffSlabRepository = tariffSlabRepository;
        this.tariffSlabMapper = tariffSlabMapper;
        this.tariffRepository = tariffRepository;
    }

    // CRUD Operations
    public List<TariffSlabDTO> getAllTariffSlabs () {
        return tariffSlabRepository.findAll ().stream ()
                .map (tariffSlabMapper::toTariffSlabDTO)
                .collect (Collectors.toList ());
    }

    public Optional<TariffSlabDTO> getTariffSlabById (String id) {
        return tariffSlabRepository.findById (id).map (tariffSlabMapper::toTariffSlabDTO);
    }

    public TariffSlabDTO createTariffSlab (TariffSlabDTO tariffSlabDTO) {
        Tariff tariff = tariffRepository.findById (tariffSlabDTO.getTariffId ()).orElseThrow ();
        TariffSlab tariffSlab = tariffSlabMapper.toTariffSlabEntity (tariffSlabDTO, tariff);
        return tariffSlabMapper.toTariffSlabDTO (tariffSlabRepository.save (tariffSlab));
    }

    public TariffSlabDTO updateTariffSlab (String id, TariffSlabDTO tariffSlabDTO) {
        TariffSlab tariffSlab = tariffSlabRepository.findById (id).orElseThrow ();
        tariffSlabMapper.updateTariffSlabEntity (tariffSlab, tariffSlabDTO);
        return tariffSlabMapper.toTariffSlabDTO (tariffSlabRepository.save (tariffSlab));
    }

    public void deleteTariffSlab (String id) {
        tariffSlabRepository.deleteById (id);
    }

    // Searching and Filtering
    public List<TariffSlabDTO> findTariffSlabsByTariffId (String tariffId) {
        return tariffSlabRepository.findByTariffId (tariffId).stream ()
                .map (tariffSlabMapper::toTariffSlabDTO)
                .collect (Collectors.toList ());
    }
}
