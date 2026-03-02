package org.example.service;

import org.example.dto.request.CommentRequest;
import org.example.dto.request.CommentUpdateRequest;
import org.example.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse create(CommentRequest request);
    List<CommentResponse> getAll();
    CommentResponse getById(Long id);
    CommentResponse update(Long id, CommentUpdateRequest request);
    void delete(Long id);

    // nested resource methods (by post)
    CommentResponse createForPost(Long postId, CommentRequest request);
    List<CommentResponse> getByPostId(Long postId);
    CommentResponse getByPostIdAndCommentId(Long postId, Long commentId);
    CommentResponse updateForPost(Long postId, Long commentId, CommentUpdateRequest request);
    void deleteForPost(Long postId, Long commentId);

    // nested resource methods (by user)
    CommentResponse createForUser(Long userId, CommentRequest request);
    List<CommentResponse> getByUserId(Long userId);
    CommentResponse getByUserIdAndCommentId(Long userId, Long commentId);
    CommentResponse updateForUser(Long userId, Long commentId, CommentUpdateRequest request);
    void deleteForUser(Long userId, Long commentId);
}
