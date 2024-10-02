package com.finzly.bbc.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUserCustomerCreationDTO {
    private List<UserCustomerCreationDTO> customers;
}
