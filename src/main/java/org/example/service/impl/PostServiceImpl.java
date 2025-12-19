package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.PostRequest;
import org.example.dto.request.PostUpdateRequest;
import org.example.dto.response.PostResponse;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.PostMapper;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostResponse create(PostRequest request) {
        Post p = PostMapper.toEntity(request);
        Post saved = postRepository.save(p);
        return PostMapper.toResponse(saved);
    }

    @Override
    public List<PostResponse> getAll() {
        return postRepository.findAll().stream().map(PostMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public PostResponse getById(Long id) {
        Post p = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post", id));
        return PostMapper.toResponse(p);
    }

    @Override
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post p = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post", id));
        PostMapper.updateEntity(p, request);
        Post saved = postRepository.save(p);
        return PostMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!postRepository.existsById(id)) throw new EntityNotFoundException("Post", id);
        postRepository.deleteById(id);
    }

    // nested resource implementations
    @Override
    public List<PostResponse> getByUserId(Long userId) {
        // ensure user exists
        if (!userRepository.existsById(userId)) throw new EntityNotFoundException("User", userId);
        return postRepository.findByUserId(userId).stream().map(PostMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public PostResponse getByUserIdAndPostId(Long userId, Long postId) {
        Post p = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Post", postId));
        return PostMapper.toResponse(p);
    }

    @Override
    public PostResponse createForUser(Long userId, PostRequest request) {
        User u = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        Post p = PostMapper.toEntity(request);
        p.setUser(u);
        Post saved = postRepository.save(p);
        return PostMapper.toResponse(saved);
    }

    @Override
    public PostResponse updateForUser(Long userId, Long postId, PostUpdateRequest request) {
        Post p = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Post", postId));
        PostMapper.updateEntity(p, request);
        Post saved = postRepository.save(p);
        return PostMapper.toResponse(saved);
    }

    @Override
    public void deleteForUser(Long userId, Long postId) {
        Post p = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Post", postId));
        postRepository.delete(p);
    }
}
