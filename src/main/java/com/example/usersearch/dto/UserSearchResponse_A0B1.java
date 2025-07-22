package com.example.usersearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchResponse_A0B1 {
    private PaginationInfo_A0B1 pagination;
    private List<UserSummary_A0B1> results;
}
```
src/main/java/com/example/usersearch/dto/UserSummary_A0B1.java
```java