package com.finzly.bbc.dtos.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginationResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
}
