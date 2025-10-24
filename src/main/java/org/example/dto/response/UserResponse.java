package org.example.dto.response;


import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String passwordHash,
    LocalDateTime createdAt,
    String status,
    String email,
    String username) {

}
