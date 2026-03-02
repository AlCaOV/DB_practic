package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(
        Long id,
        Long postId,
        Long userId,
        @NotBlank  @Size(max = 24000) String body
) {}

