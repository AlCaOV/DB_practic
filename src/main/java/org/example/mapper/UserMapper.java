package org.example.mapper;

import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.example.entity.User;

import java.time.LocalDateTime;


public class UserMapper {
    private UserMapper(){}

    // 🟢 Перетворення з DTO → Entity
    public static User toEntity(UserRequest request) {
        return User.builder()
                .email(request.email())
                .username(request.username())
                .passwordHash(request.password())
                .createdAt(LocalDateTime.now())
                .status(User.Status.active)
                .build();
    }

    // метод для оновлення наявної сутності
    public static void updateEntity(User user, org.example.dto.request.UserUpdateRequest request) {
        if (request.email() != null) user.setEmail(request.email());
        if (request.username() != null) user.setUsername(request.username());
        if (request.status() != null) {
            try {
                user.setStatus(User.Status.valueOf(request.status().toLowerCase()));
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    // 🔵 Перетворення з Entity → DTO
    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .passwordHash(user.getPasswordHash())
                .createdAt(user.getCreatedAt())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}
