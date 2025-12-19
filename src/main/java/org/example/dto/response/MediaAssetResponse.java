package org.example.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaAssetResponse {
    private Long id;
    private Long postId;
    private String kind;
    private String url;
    private Integer durationSec;
    private Integer position;
}

