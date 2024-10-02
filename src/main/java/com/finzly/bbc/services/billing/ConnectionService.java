package com.finzly.bbc.services.billing;

import com.finzly.bbc.dto.billing.ConnectionDTO;
import com.finzly.bbc.dto.billing.mapper.ConnectionMapper;
import com.finzly.bbc.exceptions.custom.ResourceNotFoundException;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.billing.Connection;
import com.finzly.bbc.models.billing.ConnectionStatus;
import com.finzly.bbc.models.billing.ConnectionType;
import com.finzly.bbc.repositories.auth.CustomerRepository;
import com.finzly.bbc.repositories.billing.ConnectionRepository;
import com.finzly.bbc.repositories.billing.ConnectionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service for Connection entity
@Service
@Transactional
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final ConnectionMapper connectionMapper;
    private final CustomerRepository customerRepository;
    private final ConnectionTypeRepository connectionTypeRepository;

    @Autowired
    public ConnectionService (ConnectionRepository connectionRepository, ConnectionMapper connectionMapper,
                              CustomerRepository customerRepository, ConnectionTypeRepository connectionTypeRepository) {
        this.connectionRepository = connectionRepository;
        this.connectionMapper = connectionMapper;
        this.customerRepository = customerRepository;
        this.connectionTypeRepository = connectionTypeRepository;
    }

    // CRUD Operations
    public List<ConnectionDTO> getAllConnections () {
        return connectionRepository.findAll ().stream ()
                .map (connectionMapper::toConnectionDTO)
                .collect (Collectors.toList ());
    }

    public Optional<ConnectionDTO> getConnectionById (String id) {
        return connectionRepository.findById (id)
                .map (connectionMapper::toConnectionDTO)
                .or (() -> {
                    throw new ResourceNotFoundException ("Connection not found with ID: " + id);
                });
    }

    public ConnectionDTO createConnection (ConnectionDTO connectionDTO) {
        Customer customer = customerRepository.findById (connectionDTO.getCustomerId ())
                .orElseThrow (() -> new ResourceNotFoundException ("Customer not found with ID: " + connectionDTO.getCustomerId ()));

        ConnectionType connectionType = connectionTypeRepository.findById (connectionDTO.getConnectionTypeId ())
                .orElseThrow (() -> new ResourceNotFoundException ("ConnectionType not found with ID: " + connectionDTO.getConnectionTypeId ()));

        Connection connection = connectionMapper.toConnectionEntity (connectionDTO, customer, connectionType);
        return connectionMapper.toConnectionDTO (connectionRepository.save (connection));
    }

    public ConnectionDTO updateConnection (String id, ConnectionDTO connectionDTO) {
        Connection connection = connectionRepository.findById (id)
                .orElseThrow (() -> new ResourceNotFoundException ("Connection not found with ID: " + id));

        connectionMapper.updateConnectionEntity (connection, connectionDTO);
        return connectionMapper.toConnectionDTO (connectionRepository.save (connection));
    }

    public void deleteConnection (String id) {
        if (!connectionRepository.existsById (id)) {
            throw new ResourceNotFoundException ("Connection not found with ID: " + id);
        }
        connectionRepository.deleteById (id);
    }

    // Searching and Filtering
    public List<ConnectionDTO> searchConnectionsByCustomer (String customerId) {
        return connectionRepository.findByCustomer_CustomerId (customerId).stream ()
                .map (connectionMapper::toConnectionDTO)
                .collect (Collectors.toList ());
    }

    public List<ConnectionDTO> findActiveConnections () {
        return connectionRepository.findByStatus (ConnectionStatus.ACTIVE).stream ()
                .map (connectionMapper::toConnectionDTO)
                .collect (Collectors.toList ());
    }
}
