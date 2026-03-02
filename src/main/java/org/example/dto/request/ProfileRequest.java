package org.example.dto.request;

public record ProfileRequest(Long userId, String fullName, String bio, String avatarUrl) {}

