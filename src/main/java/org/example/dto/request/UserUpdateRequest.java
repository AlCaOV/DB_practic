package org.example.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        Long id,
        @Email String email,
        @Size(min = 3, max = 50) String username,
        String password,
        String status
) {}

