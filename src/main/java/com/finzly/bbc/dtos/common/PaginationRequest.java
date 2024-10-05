package com.finzly.bbc.dtos.common;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {

    @Min(0)
    private Integer page;
    @Min(1)
    private Integer size;
}
