package org.example.dto.request;

public record FriendshipRequest(Long id, Long followerId, Long followeeId, String status) {}

