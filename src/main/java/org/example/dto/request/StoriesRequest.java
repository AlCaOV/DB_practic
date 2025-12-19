package org.example.dto.request;

public record StoriesRequest(Long id, Long userId, String mediaUrl, String kind, String caption, Integer durationSec) {}

