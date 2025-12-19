package org.example.dto.request;

import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(
        Long id,
        Long postId,
        Long userId,
        @Size(max = 1000) String body
) {}

