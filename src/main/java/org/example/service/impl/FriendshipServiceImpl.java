package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.FriendshipRequest;
import org.example.dto.request.FriendshipUpdateRequest;
import org.example.dto.response.FriendshipResponse;
import org.example.entity.Friendship;
import org.example.entity.User;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.FriendshipMapper;
import org.example.repository.FriendshipRepository;
import org.example.service.FriendshipService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository repo;

    @Override
    public FriendshipResponse create(FriendshipRequest request) { Friendship f = FriendshipMapper.toEntity(request); Friendship saved = repo.save(f); return FriendshipMapper.toResponse(saved); }

    @Override
    public List<FriendshipResponse> getAll() { return repo.findAll().stream().map(FriendshipMapper::toResponse).collect(Collectors.toList()); }

    @Override
    public FriendshipResponse getById(Long id) { Friendship f = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Friendship", id)); return FriendshipMapper.toResponse(f); }

    @Override
    public FriendshipResponse update(Long id, FriendshipUpdateRequest request) { Friendship f = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Friendship", id)); FriendshipMapper.updateEntity(f, request); Friendship saved = repo.save(f); return FriendshipMapper.toResponse(saved); }

    @Override
    public void delete(Long id) { if (!repo.existsById(id)) throw new EntityNotFoundException("Friendship", id); repo.deleteById(id); }

    // --- nested resource methods (by user as followee/follower)
    @Override
    public List<FriendshipResponse> getFollowersForUser(Long userId) {
        return repo.findByFolloweeId(userId).stream().map(FriendshipMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FriendshipResponse> getFolloweesForUser(Long userId) {
        return repo.findByFollowerId(userId).stream().map(FriendshipMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public FriendshipResponse getByIdAndFolloweeId(Long id, Long followeeId) {
        Friendship f = repo.findByIdAndFolloweeId(id, followeeId).orElseThrow(() -> new EntityNotFoundException("Friendship", id));
        return FriendshipMapper.toResponse(f);
    }

    @Override
    public FriendshipResponse getByIdAndFollowerId(Long id, Long followerId) {
        Friendship f = repo.findByIdAndFollowerId(id, followerId).orElseThrow(() -> new EntityNotFoundException("Friendship", id));
        return FriendshipMapper.toResponse(f);
    }

    @Override
    public FriendshipResponse createForFollower(Long followerId, FriendshipRequest request) {
        Friendship reqEntity = FriendshipMapper.toEntity(request);
        if (reqEntity.getFollower() == null) {
            User u = new User(); u.setId(followerId); reqEntity.setFollower(u);
        }
        Friendship saved = repo.save(reqEntity);
        return FriendshipMapper.toResponse(saved);
    }

    @Override
    public void deleteForUser(Long userId, Long friendshipId) {
        // delete only if friendship exists and involves given user as follower or followee
        Friendship f = repo.findById(friendshipId).orElseThrow(() -> new EntityNotFoundException("Friendship", friendshipId));
        if (f.getFollower() == null && f.getFollowee() == null) throw new EntityNotFoundException("Friendship", friendshipId);
        if (!userId.equals(f.getFollower() != null ? f.getFollower().getId() : null) && !userId.equals(f.getFollowee() != null ? f.getFollowee().getId() : null)) {
            throw new EntityNotFoundException("Friendship", friendshipId);
        }
        repo.delete(f);
    }

    // --- nested resource methods (by user acting as parent resource)
    @Override
    public FriendshipResponse createForUser(Long userId, FriendshipRequest request) {
        Friendship reqEntity = FriendshipMapper.toEntity(request);
        // assume parent user is follower by default if followerId not set
        if (reqEntity.getFollower() == null) { User u = new User(); u.setId(userId); reqEntity.setFollower(u); }
        Friendship saved = repo.save(reqEntity);
        return FriendshipMapper.toResponse(saved);
    }

    @Override
    public FriendshipResponse updateForUser(Long userId, Long friendshipId, FriendshipUpdateRequest request) {
        Friendship f = repo.findById(friendshipId).orElseThrow(() -> new EntityNotFoundException("Friendship", friendshipId));
        // ensure this friendship is related to the parent user
        if (!userId.equals(f.getFollower() != null ? f.getFollower().getId() : null) && !userId.equals(f.getFollowee() != null ? f.getFollowee().getId() : null)) {
            throw new EntityNotFoundException("Friendship", friendshipId);
        }
        FriendshipMapper.updateEntity(f, request);
        Friendship saved = repo.save(f);
        return FriendshipMapper.toResponse(saved);
    }

    @Override
    public List<FriendshipResponse> getByUserId(Long userId) {
        return repo.findByFollowerIdOrFolloweeId(userId, userId).stream().map(FriendshipMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public FriendshipResponse getByUserIdAndFriendshipId(Long userId, Long friendshipId) {
        Friendship f = repo.findById(friendshipId).orElseThrow(() -> new EntityNotFoundException("Friendship", friendshipId));
        if (!userId.equals(f.getFollower() != null ? f.getFollower().getId() : null) && !userId.equals(f.getFollowee() != null ? f.getFollowee().getId() : null)) {
            throw new EntityNotFoundException("Friendship", friendshipId);
        }
        return FriendshipMapper.toResponse(f);
    }
}
