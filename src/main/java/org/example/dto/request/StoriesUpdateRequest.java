package org.example.dto.request;

public record StoriesUpdateRequest(Long id, Long userId, String mediaUrl, String kind, String caption, Integer durationSec) {}

