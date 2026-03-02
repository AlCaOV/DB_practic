package org.example.mapper;

import org.example.dto.request.FriendshipRequest;
import org.example.dto.request.FriendshipUpdateRequest;
import org.example.dto.response.FriendshipResponse;
import org.example.entity.Friendship;
import org.example.entity.User;

public class FriendshipMapper {
    private FriendshipMapper() {}

    public static Friendship toEntity(FriendshipRequest r) {
        Friendship f = new Friendship();
        if (r.followerId() != null) { User u = new User(); u.setId(r.followerId()); f.setFollower(u); }
        if (r.followeeId() != null) { User u = new User(); u.setId(r.followeeId()); f.setFollowee(u); }
        if (r.status() != null) f.setStatus(Friendship.Status.valueOf(r.status().toLowerCase()));
        return f;
    }

    public static void updateEntity(Friendship f, FriendshipUpdateRequest r) {
        if (r.followerId() != null) { User u = new User(); u.setId(r.followerId()); f.setFollower(u); }
        if (r.followeeId() != null) { User u = new User(); u.setId(r.followeeId()); f.setFollowee(u); }
        if (r.status() != null) f.setStatus(Friendship.Status.valueOf(r.status().toLowerCase()));
    }

    public static FriendshipResponse toResponse(Friendship f) {
        return FriendshipResponse.builder().id(f.getId()).followerId(f.getFollower() != null ? f.getFollower().getId() : null).followeeId(f.getFollowee() != null ? f.getFollowee().getId() : null).status(f.getStatus() != null ? f.getStatus().name() : null).build();
    }
}

