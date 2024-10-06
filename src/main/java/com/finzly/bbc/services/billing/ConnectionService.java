package com.finzly.bbc.services.billing;

import com.finzly.bbc.dtos.billing.ConnectionRequest;
import com.finzly.bbc.dtos.billing.ConnectionResponse;
import com.finzly.bbc.dtos.common.PaginationRequest;
import com.finzly.bbc.dtos.common.PaginationResponse;
import com.finzly.bbc.exceptions.BadRequestException;
import com.finzly.bbc.exceptions.ResourceNotFoundException;
import com.finzly.bbc.models.billing.Connection;
import com.finzly.bbc.models.billing.ConnectionStatus;
import com.finzly.bbc.models.billing.ConnectionType;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.repositories.billing.ConnectionRepository;
import com.finzly.bbc.repositories.billing.ConnectionTypeRepository;
import com.finzly.bbc.repositories.auth.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final CustomerRepository customerRepository;
    private final ConnectionTypeRepository connectionTypeRepository;
    private final ModelMapper modelMapper;

    // Create a new connection
    public ConnectionResponse createConnection(ConnectionRequest connectionRequest) {
        if (connectionRequest.getCustomerId() == null || connectionRequest.getConnectionTypeId() == null) {
            throw new BadRequestException("Customer ID and Connection Type ID are mandatory.");
        }

        Customer customer = customerRepository.findById(connectionRequest.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + connectionRequest.getCustomerId()));

        ConnectionType connectionType = connectionTypeRepository.findById(connectionRequest.getConnectionTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Connection Type not found with ID: " + connectionRequest.getConnectionTypeId()));

        Connection connection = new Connection();
        connection.setCustomer(customer);
        connection.setConnectionType(connectionType);
        connection.setStatus(ConnectionStatus.ACTIVE);
        Connection savedConnection = connectionRepository.save(connection);

        return modelMapper.map(savedConnection, ConnectionResponse.class);
    }

    // Read connection by ID
    public ConnectionResponse getConnectionById(String id) {
        Connection connection = connectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Connection not found with ID: " + id));

        // Manually map the connection and related customer and connection type details
        return ConnectionResponse.builder()
                .id(connection.getId())
                .customerId(connection.getCustomer().getCustomerId ())
                .customerName(connection.getCustomer().getUser ().getFirstName () + " " + connection.getCustomer().getUser ().getLastName ())
                .customerEmail(connection.getCustomer().getUser().getEmail())
                .connectionTypeId(connection.getConnectionType().getId())
                .connectionTypeName(connection.getConnectionType().getTypeName())
                .startDate(connection.getStartDate())
                .status(connection.getStatus().name())
                .build();
    }


    // Update connection details
    public ConnectionResponse updateConnection(String id, ConnectionRequest connectionRequest) {
        Connection connection = connectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Connection not found with ID: " + id));

        // Update fields if they are present in the request
        if (connectionRequest.getCustomerId() != null) {
            Customer customer = customerRepository.findById(connectionRequest.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + connectionRequest.getCustomerId()));
            connection.setCustomer(customer);
        }
        if (connectionRequest.getConnectionTypeId() != null) {
            ConnectionType connectionType = connectionTypeRepository.findById(connectionRequest.getConnectionTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Connection Type not found with ID: " + connectionRequest.getConnectionTypeId()));
            connection.setConnectionType(connectionType);
        }
        if (connectionRequest.getStatus() != null) {
            connection.setStatus(ConnectionStatus.valueOf(connectionRequest.getStatus()));
        }

        Connection updatedConnection = connectionRepository.save(connection);
        return modelMapper.map(updatedConnection, ConnectionResponse.class);
    }

    // Delete connection by ID
    public void deleteConnection(String id) {
        Connection connection = connectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Connection not found with ID: " + id));
        connectionRepository.delete(connection);
    }

    // Search connections with pagination
    public PaginationResponse<ConnectionResponse> searchConnectionsWithPagination(
            String customerId, String connectionTypeId, String status, PaginationRequest paginationRequest) {

        Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize());
        Page<Connection> connectionPage = connectionRepository.searchConnections(customerId, connectionTypeId, status, pageable);

        List<ConnectionResponse> connectionResponses = connectionPage.getContent().stream()
                .map(connection -> ConnectionResponse.builder()
                        .id(connection.getId())
                        .customerId(connection.getCustomer().getCustomerId())
                        .customerName(connection.getCustomer().getUser().getFirstName() + " " + connection.getCustomer().getUser().getLastName())
                        .customerEmail(connection.getCustomer().getUser().getEmail())
                        .connectionTypeId(connection.getConnectionType().getId())
                        .connectionTypeName(connection.getConnectionType().getTypeName())
                        .startDate(connection.getStartDate())
                        .status(connection.getStatus().name())
                        .build())
                .toList();

        return PaginationResponse.<ConnectionResponse>builder()
                .content(connectionResponses)
                .totalPages(connectionPage.getTotalPages())
                .totalElements(connectionPage.getTotalElements())
                .size(connectionPage.getSize())
                .number(connectionPage.getNumber())
                .build();
    }

}
