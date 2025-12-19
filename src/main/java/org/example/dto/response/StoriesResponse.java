package org.example.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoriesResponse {
    private Long id;
    private Long userId;
    private String mediaUrl;
    private String kind;
    private String caption;
    private Integer durationSec;
}

