package org.example.service;

import org.example.dto.request.FriendshipRequest;
import org.example.dto.request.FriendshipUpdateRequest;
import org.example.dto.response.FriendshipResponse;

import java.util.List;

public interface FriendshipService {
    FriendshipResponse create(FriendshipRequest request);
    List<FriendshipResponse> getAll();
    FriendshipResponse getById(Long id);
    FriendshipResponse update(Long id, FriendshipUpdateRequest request);
    void delete(Long id);

    // nested resource methods (by user)
    List<FriendshipResponse> getFollowersForUser(Long userId);
    List<FriendshipResponse> getFolloweesForUser(Long userId);
    FriendshipResponse getByIdAndFolloweeId(Long id, Long followeeId);
    FriendshipResponse getByIdAndFollowerId(Long id, Long followerId);
    FriendshipResponse createForFollower(Long followerId, FriendshipRequest request);
    void deleteForUser(Long userId, Long friendshipId);

    // nested resource methods (by user acting as parent resource)
    FriendshipResponse createForUser(Long userId, FriendshipRequest request);
    FriendshipResponse updateForUser(Long userId, Long friendshipId, FriendshipUpdateRequest request);
    List<FriendshipResponse> getByUserId(Long userId);
    FriendshipResponse getByUserIdAndFriendshipId(Long userId, Long friendshipId);
}
