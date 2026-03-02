package org.example.service;

import org.example.dto.request.StoriesRequest;
import org.example.dto.request.StoriesUpdateRequest;
import org.example.dto.response.StoriesResponse;

import java.util.List;

public interface StoriesService {
    StoriesResponse create(StoriesRequest request);
    List<StoriesResponse> getAll();
    StoriesResponse getById(Long id);
    StoriesResponse update(Long id, StoriesUpdateRequest request);
    void delete(Long id);

    // nested resource methods (by user)
    List<StoriesResponse> getByUserId(Long userId);
    StoriesResponse getByUserIdAndStoryId(Long userId, Long storyId);
    StoriesResponse createForUser(Long userId, StoriesRequest request);
    StoriesResponse updateForUser(Long userId, Long storyId, StoriesUpdateRequest request);
    void deleteForUser(Long userId, Long storyId);
}
