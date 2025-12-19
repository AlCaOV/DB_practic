package org.example.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {
    private Long id;
    private Long userId;
    private String caption;
    private LocalDateTime createdAt;
    private String location;
    private boolean isArchived;
    private String status;
}

