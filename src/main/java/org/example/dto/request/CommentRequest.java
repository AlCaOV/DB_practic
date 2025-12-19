package org.example.dto.request;

import jakarta.validation.constraints.Size;

public record CommentRequest(
        Long id,
        Long postId,
        Long userId,
        @Size(max = 1000) String body
) {}

