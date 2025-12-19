package org.example.dto.request;

import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        Long id,
        Long userId,
        @Size(max = 1000) String caption,
        String location
) {}

