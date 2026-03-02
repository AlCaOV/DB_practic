package org.example.service;

import org.example.dto.request.LikesRequest;
import org.example.dto.request.LikesUpdateRequest;
import org.example.dto.response.LikesResponse;

import java.util.List;

public interface LikesService {
    LikesResponse create(LikesRequest request);
    List<LikesResponse> getAll();
    LikesResponse getById(Long id);
    LikesResponse update(Long id, LikesUpdateRequest request);
    void delete(Long id);

    // nested resource methods (by post)
    LikesResponse createForPost(Long postId, LikesRequest request);
    List<LikesResponse> getByPostId(Long postId);
    LikesResponse getByPostIdAndLikeId(Long postId, Long likeId);
    LikesResponse updateForPost(Long postId, Long likeId, LikesUpdateRequest request);
    void deleteForPost(Long postId, Long likeId);

    // nested resource methods (by user)
    List<LikesResponse> getByUserId(Long userId);
    LikesResponse getByUserIdAndLikeId(Long userId, Long likeId);
}
