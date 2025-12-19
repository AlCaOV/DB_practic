package org.example.dto.request;

public record ProfileUpdateRequest(Long userId, String fullName, String bio, String avatarUrl) {}

