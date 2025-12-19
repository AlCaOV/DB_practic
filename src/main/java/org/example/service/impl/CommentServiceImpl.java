package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CommentRequest;
import org.example.dto.request.CommentUpdateRequest;
import org.example.dto.response.CommentResponse;
import org.example.entity.Comment;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.CommentMapper;
import org.example.repository.CommentRepository;
import org.example.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public CommentResponse create(CommentRequest request) {
        Comment c = CommentMapper.toEntity(request);
        Comment saved = commentRepository.save(c);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public List<CommentResponse> getAll() {
        return commentRepository.findAll().stream().map(CommentMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public CommentResponse getById(Long id) {
        Comment c = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment", id));
        return CommentMapper.toResponse(c);
    }

    @Override
    public CommentResponse update(Long id, CommentUpdateRequest request) {
        Comment c = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment", id));
        CommentMapper.updateEntity(c, request);
        Comment saved = commentRepository.save(c);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!commentRepository.existsById(id)) throw new EntityNotFoundException("Comment", id);
        commentRepository.deleteById(id);
    }

    // --- nested resource methods (by post)
    @Override
    public CommentResponse createForPost(Long postId, CommentRequest request) {
        // force postId into request entity
        Comment reqEntity = CommentMapper.toEntity(request);
        if (reqEntity.getPost() == null) { org.example.entity.Post p = new org.example.entity.Post(); p.setId(postId); reqEntity.setPost(p); }
        Comment saved = commentRepository.save(reqEntity);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public List<CommentResponse> getByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream().map(CommentMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public CommentResponse getByPostIdAndCommentId(Long postId, Long commentId) {
        Comment c = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        return CommentMapper.toResponse(c);
    }

    @Override
    public CommentResponse updateForPost(Long postId, Long commentId, CommentUpdateRequest request) {
        Comment c = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        CommentMapper.updateEntity(c, request);
        Comment saved = commentRepository.save(c);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public void deleteForPost(Long postId, Long commentId) {
        Comment c = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        commentRepository.delete(c);
    }

    // --- nested resource methods (by user)
    @Override
    public CommentResponse createForUser(Long userId, CommentRequest request) {
        Comment reqEntity = CommentMapper.toEntity(request);
        if (reqEntity.getUser() == null) { org.example.entity.User u = new org.example.entity.User(); u.setId(userId); reqEntity.setUser(u); }
        Comment saved = commentRepository.save(reqEntity);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public List<CommentResponse> getByUserId(Long userId) {
        return commentRepository.findByUserId(userId).stream().map(CommentMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public CommentResponse getByUserIdAndCommentId(Long userId, Long commentId) {
        Comment c = commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        return CommentMapper.toResponse(c);
    }

    @Override
    public CommentResponse updateForUser(Long userId, Long commentId, CommentUpdateRequest request) {
        Comment c = commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        CommentMapper.updateEntity(c, request);
        Comment saved = commentRepository.save(c);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public void deleteForUser(Long userId, Long commentId) {
        Comment c = commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        commentRepository.delete(c);
    }
}
