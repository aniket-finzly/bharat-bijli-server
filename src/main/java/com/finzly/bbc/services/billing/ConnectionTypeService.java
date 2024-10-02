package com.finzly.bbc.services.billing;

import com.finzly.bbc.dto.billing.ConnectionTypeDTO;
import com.finzly.bbc.dto.billing.mapper.ConnectionTypeMapper;
import com.finzly.bbc.models.billing.ConnectionType;
import com.finzly.bbc.repositories.billing.ConnectionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service for ConnectionType entity
@Service
@Transactional
public class ConnectionTypeService {

    private final ConnectionTypeRepository connectionTypeRepository;
    private final ConnectionTypeMapper connectionTypeMapper;

    @Autowired
    public ConnectionTypeService (ConnectionTypeRepository connectionTypeRepository, ConnectionTypeMapper connectionTypeMapper) {
        this.connectionTypeRepository = connectionTypeRepository;
        this.connectionTypeMapper = connectionTypeMapper;
    }

    // CRUD Operations
    public List<ConnectionTypeDTO> getAllConnectionTypes () {
        return connectionTypeRepository.findAll ().stream ()
                .map (connectionTypeMapper::toConnectionTypeDTO)
                .collect (Collectors.toList ());
    }

    public Optional<ConnectionTypeDTO> getConnectionTypeById (String id) {
        return connectionTypeRepository.findById (id).map (connectionTypeMapper::toConnectionTypeDTO);
    }

    public ConnectionTypeDTO createConnectionType (ConnectionTypeDTO connectionTypeDTO) {
        ConnectionType connectionType = connectionTypeMapper.toConnectionTypeEntity (connectionTypeDTO);
        return connectionTypeMapper.toConnectionTypeDTO (connectionTypeRepository.save (connectionType));
    }

    public ConnectionTypeDTO updateConnectionType (String id, ConnectionTypeDTO connectionTypeDTO) {
        ConnectionType connectionType = connectionTypeRepository.findById (id).orElseThrow ();
        connectionTypeMapper.updateConnectionTypeEntity (connectionType, connectionTypeDTO);
        return connectionTypeMapper.toConnectionTypeDTO (connectionTypeRepository.save (connectionType));
    }

    public void deleteConnectionType (String id) {
        connectionTypeRepository.deleteById (id);
    }
}
