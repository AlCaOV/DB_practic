package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.LikesRequest;
import org.example.dto.request.LikesUpdateRequest;
import org.example.dto.response.LikesResponse;
import org.example.entity.Likes;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.LikesMapper;
import org.example.repository.LikesRepository;
import org.example.repository.PostRepository;
import org.example.service.AuthService;
import org.example.service.LikesService;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {
    private final LikesRepository repo;
    private final PostRepository postRepository;
    private final AuthService userHelper;

    @Override
    public LikesResponse create(LikesRequest request) {
        User currentUser = userHelper.getCurrentUser();
        Likes l = LikesMapper.toEntity(request);
        l.setUser(currentUser);
        Likes saved = repo.save(l);
        return LikesMapper.toResponse(saved);
    }

    @Override
    public List<LikesResponse> getAll() {
        return repo.findAll().stream().map(LikesMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public LikesResponse getById(Long id) {
        Likes l = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Likes", id));
        return LikesMapper.toResponse(l);
    }

    @Override
    public LikesResponse update(Long id, LikesUpdateRequest request) {
        // Лайки рідко оновлюються, але якщо треба:
        Likes l = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Likes", id));
        User currentUser = userHelper.getCurrentUser();
        Long ownerId = (l.getUser() != null) ? l.getUser().getId() : -1L;

        if (!ownerId.equals(currentUser.getId())) {
            throw new AccessDeniedException("Ви не можете змінювати чужий лайк");
        }
        LikesMapper.updateEntity(l, request);
        Likes saved = repo.save(l);
        return LikesMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Likes l = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Likes", id));
        checkDeletePermissions(l);
        repo.delete(l);
    }

    @Override
    public LikesResponse createForPost(Long postId, LikesRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post", postId));
        User currentUser = userHelper.getCurrentUser();

        // Перевірка дублів (опціонально)
        // if (repo.existsByPostAndUser(post, currentUser)) { throw new RuntimeException("Post already liked"); }

        Likes like = LikesMapper.toEntity(request);
        like.setPost(post);
        like.setUser(currentUser);
        Likes saved = repo.save(like);
        return LikesMapper.toResponse(saved);
    }

    @Override
    public List<LikesResponse> getByPostId(Long postId) {
        return repo.findByPostId(postId).stream().map(LikesMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public LikesResponse getByPostIdAndLikeId(Long postId, Long likeId) {
        Likes l = repo.findByIdAndPostId(likeId, postId).orElseThrow(() -> new EntityNotFoundException("Likes", likeId));
        return LikesMapper.toResponse(l);
    }

    @Override
    public LikesResponse updateForPost(Long postId, Long likeId, LikesUpdateRequest request) {
        Likes l = repo.findByIdAndPostId(likeId, postId).orElseThrow(() -> new EntityNotFoundException("Likes", likeId));
        // тут теж можна перевірку
        LikesMapper.updateEntity(l, request);
        Likes saved = repo.save(l);
        return LikesMapper.toResponse(saved);
    }

    @Override
    public void deleteForPost(Long postId, Long likeId) {
        Likes like = repo.findByIdAndPostId(likeId, postId)
                .orElseThrow(() -> new EntityNotFoundException("Like", likeId));
        checkDeletePermissions(like);
        repo.delete(like);
    }

    private void checkDeletePermissions(Likes like) {
        User currentUser = userHelper.getCurrentUser();
        Long likeAuthorId = (like.getUser() != null) ? like.getUser().getId() : -1L;

        boolean isOwner = likeAuthorId.equals(currentUser.getId());
        boolean isAdmin = userHelper.isAdmin();

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Ви не можете видалити чужий лайк");
        }
    }

    @Override
    public List<LikesResponse> getByUserId(Long userId) {
        return repo.findByUserId(userId).stream().map(LikesMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public LikesResponse getByUserIdAndLikeId(Long userId, Long likeId) {
        Likes l = repo.findByIdAndUserId(likeId, userId).orElseThrow(() -> new EntityNotFoundException("Likes", likeId));
        return LikesMapper.toResponse(l);
    }
}