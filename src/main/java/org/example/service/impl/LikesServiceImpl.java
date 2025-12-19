package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.LikesRequest;
import org.example.dto.request.LikesUpdateRequest;
import org.example.dto.response.LikesResponse;
import org.example.entity.Likes;
import org.example.entity.Post;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.LikesMapper;
import org.example.repository.LikesRepository;
import org.example.repository.PostRepository;
import org.example.service.LikesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {
    private final LikesRepository repo;
    private final PostRepository postRepository;

    @Override
    public LikesResponse create(LikesRequest request) { Likes l = LikesMapper.toEntity(request); Likes saved = repo.save(l); return LikesMapper.toResponse(saved); }

    @Override
    public List<LikesResponse> getAll() { return repo.findAll().stream().map(LikesMapper::toResponse).collect(Collectors.toList()); }

    @Override
    public LikesResponse getById(Long id) { Likes l = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Likes", id)); return LikesMapper.toResponse(l); }

    @Override
    public LikesResponse update(Long id, LikesUpdateRequest request) { Likes l = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Likes", id)); LikesMapper.updateEntity(l, request); Likes saved = repo.save(l); return LikesMapper.toResponse(saved); }

    @Override
    public void delete(Long id) { if (!repo.existsById(id)) throw new EntityNotFoundException("Likes", id); repo.deleteById(id); }

    // --- nested resource methods (by post)
    @Override
    public LikesResponse createForPost(Long postId, LikesRequest request) {
        Likes reqEntity = LikesMapper.toEntity(request);
        if (reqEntity.getPost() == null) {
            Post p = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post", postId));
            reqEntity.setPost(p);
        }
        Likes saved = repo.save(reqEntity);
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
        LikesMapper.updateEntity(l, request);
        Likes saved = repo.save(l);
        return LikesMapper.toResponse(saved);
    }

    @Override
    public void deleteForPost(Long postId, Long likeId) {
        Likes l = repo.findByIdAndPostId(likeId, postId).orElseThrow(() -> new EntityNotFoundException("Likes", likeId));
        repo.delete(l);
    }

    // --- nested resource methods (by user)
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
