package org.example.dto.request;

public record MediaAssetRequest(Long id, Long postId, String kind, String url, Integer durationSec, Integer position) {}

