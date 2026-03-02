package org.example.dto.request;

public record FriendshipUpdateRequest(Long id, Long followerId, Long followeeId, String status) {}

