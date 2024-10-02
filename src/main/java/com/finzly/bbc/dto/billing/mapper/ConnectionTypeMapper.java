package com.finzly.bbc.dto.billing.mapper;

import com.finzly.bbc.dto.billing.ConnectionTypeDTO;
import com.finzly.bbc.models.billing.ConnectionType;
import org.springframework.stereotype.Component;

// Mapper for ConnectionType entity
@Component
public class ConnectionTypeMapper {

    public ConnectionTypeDTO toConnectionTypeDTO (ConnectionType connectionType) {
        return new ConnectionTypeDTO (
                connectionType.getId (),
                connectionType.getTypeName (),
                connectionType.getDescription ()
        );
    }

    public ConnectionType toConnectionTypeEntity (ConnectionTypeDTO connectionTypeDTO) {
        return new ConnectionType (
                connectionTypeDTO.getId (),
                connectionTypeDTO.getTypeName (),
                connectionTypeDTO.getDescription (),
                null // assuming connections list will be set elsewhere
        );
    }

    public void updateConnectionTypeEntity (ConnectionType connectionType, ConnectionTypeDTO connectionTypeDTO) {
        // Update the existing ConnectionType entity with new values
        connectionType.setTypeName (connectionTypeDTO.getTypeName ());
        connectionType.setDescription (connectionTypeDTO.getDescription ());
        // Note: The connections list is assumed to be handled elsewhere
    }
}
