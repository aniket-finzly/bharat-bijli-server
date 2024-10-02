package com.finzly.bbc.dto.billing.mapper;

import com.finzly.bbc.dto.billing.ConnectionDTO;
import com.finzly.bbc.models.auth.Customer;
import com.finzly.bbc.models.billing.Connection;
import com.finzly.bbc.models.billing.ConnectionStatus;
import com.finzly.bbc.models.billing.ConnectionType;
import org.springframework.stereotype.Component;

@Component
public class ConnectionMapper {

    public ConnectionDTO toConnectionDTO (Connection connection) {
        return new ConnectionDTO (
                connection.getId (),
                connection.getCustomer ().getCustomerId (),
                connection.getConnectionType ().getId (),
                connection.getStartDate (),
                connection.getStatus ().name ()
        );
    }

    public Connection toConnectionEntity (ConnectionDTO connectionDTO, Customer customer, ConnectionType connectionType) {
        return new Connection (
                connectionDTO.getId (),
                customer,
                connectionType,
                connectionDTO.getStartDate (),
                ConnectionStatus.valueOf (connectionDTO.getStatus ()),
                null // Assuming this would be set later as invoices
        );
    }

    public void updateConnectionEntity (Connection connection, ConnectionDTO connectionDTO) {
        // Update the existing Connection entity with new values
        connection.setStartDate (connectionDTO.getStartDate ());
        connection.setStatus (ConnectionStatus.valueOf (connectionDTO.getStatus ()));
        // Note: Customer and ConnectionType relationships are assumed to be handled elsewhere
    }
}
