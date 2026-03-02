package org.example.service;

import org.example.dto.request.MediaAssetRequest;
import org.example.dto.request.MediaAssetUpdateRequest;
import org.example.dto.response.MediaAssetResponse;

import java.util.List;

public interface MediaAssetService {
    MediaAssetResponse create(MediaAssetRequest request);
    List<MediaAssetResponse> getAll();
    MediaAssetResponse getById(Long id);
    MediaAssetResponse update(Long id, MediaAssetUpdateRequest request);
    void delete(Long id);

    // nested resource methods for /posts/{postId}/media-assets
    List<MediaAssetResponse> getByPostId(Long postId);
    MediaAssetResponse getByPostIdAndMediaId(Long postId, Long mediaId);
    MediaAssetResponse createForPost(Long postId, MediaAssetRequest request);
    MediaAssetResponse updateForPost(Long postId, Long mediaId, MediaAssetUpdateRequest request);
    void deleteForPost(Long postId, Long mediaId);
}
