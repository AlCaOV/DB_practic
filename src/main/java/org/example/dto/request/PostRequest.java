package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        Long id,
        Long userId,
        @NotBlank  @Size(max = 24000) String caption,
        String location
) {}

