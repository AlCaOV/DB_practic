package org.example.repository;

import org.example.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    // find friendships where the given user is the followee (they are being followed)
    List<Friendship> findByFolloweeId(Long followeeId);
    // find friendships where the given user is the follower (they follow others)
    List<Friendship> findByFollowerId(Long followerId);

    Optional<Friendship> findByIdAndFolloweeId(Long id, Long followeeId);
    Optional<Friendship> findByIdAndFollowerId(Long id, Long followerId);

    // helper to get all friendships where user participates
    List<Friendship> findByFollowerIdOrFolloweeId(Long followerId, Long followeeId);
}
