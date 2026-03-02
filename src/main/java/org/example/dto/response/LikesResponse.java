package org.example.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikesResponse {
    private Long id;
    private Long postId;
    private Long userId;
}

