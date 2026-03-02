package org.example.dto.request;

public record MediaAssetUpdateRequest(Long id, Long postId, String kind, String url, Integer durationSec, Integer position) {}

