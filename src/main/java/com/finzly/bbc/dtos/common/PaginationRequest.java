package com.finzly.bbc.dtos.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationRequest {

    private Integer page = 0;  // Default to 0
    private Integer size = 10;  // Default to 10
    private String sortBy = "createdAt";  // Default sortBy
    private String sortDirection = "asc";  // Default direction
}
