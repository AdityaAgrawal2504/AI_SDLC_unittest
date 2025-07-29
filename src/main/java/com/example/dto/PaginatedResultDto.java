src/main/java/com/example/dto/PaginatedResultDto.java
package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic DTO for paginated API responses.
 * @param <T> The type of the items in the list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResultDto<T> {
    private List<T> items;
    private int page;
    private int pageSize;
    private long total;
}