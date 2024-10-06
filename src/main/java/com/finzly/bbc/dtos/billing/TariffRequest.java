package com.finzly.bbc.dtos.billing;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffRequest {
    private String connectionTypeId;

    @Min(value = 0, message = "Minimum units must be greater than or equal to 0.")
    private Integer minUnits;

    @Min(value = 1, message = "Maximum units must be greater than 0.")
    private Integer maxUnits;

    @Positive(message = "Rate per unit must be greater than 0.")
    private Double ratePerUnit;
}
