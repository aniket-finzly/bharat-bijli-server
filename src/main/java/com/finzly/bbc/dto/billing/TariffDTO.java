package com.finzly.bbc.dto.billing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffDTO {
    private String id;
    private String connectionTypeId;
    private String description;
}
