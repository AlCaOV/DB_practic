package org.example.mapper;

import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.example.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


public class UserMapper {
    private UserMapper(){}

    // 🟢 Перетворення з DTO → Entity
    public User toEntity(UserRequest request) {
        return User.builder()
                .email(request.email())
                .username(request.username())
                .passwordHash(request.password())
                .createdAt(LocalDateTime.now())
                .status(User.Status.active)
                .build();
    }

    // 🔵 Перетворення з Entity → DTO
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .passwordHash(user.getPasswordHash())
                .createdAt(user.getCreatedAt())
                .status(user.getStatus().name())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}
