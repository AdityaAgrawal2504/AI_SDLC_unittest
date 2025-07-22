package com.example.usersearch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationInfo_A0B1 {
    private int currentPage;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
```
src/main/java/com/example/usersearch/dto/ErrorResponse_A0B1.java
```java