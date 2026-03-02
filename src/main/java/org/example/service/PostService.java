package org.example.service;

import org.example.dto.request.PostRequest;
import org.example.dto.request.PostUpdateRequest;
import org.example.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    PostResponse create(PostRequest request);
    List<PostResponse> getAll();
    PostResponse getById(Long id);
    PostResponse update(Long id, PostUpdateRequest request);
    void delete(Long id);

    // nested resource methods
    List<PostResponse> getByUserId(Long userId);
    PostResponse getByUserIdAndPostId(Long userId, Long postId);
    PostResponse createForUser(Long userId, PostRequest request);
    PostResponse updateForUser(Long userId, Long postId, PostUpdateRequest request);
    void deleteForUser(Long userId, Long postId);
}
