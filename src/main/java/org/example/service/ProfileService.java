package org.example.service;

import org.example.dto.request.ProfileRequest;
import org.example.dto.request.ProfileUpdateRequest;
import org.example.dto.response.ProfileResponse;

import java.util.List;

public interface ProfileService {
    ProfileResponse create(ProfileRequest request);
    List<ProfileResponse> getAll();
    ProfileResponse getById(Long userId);
    ProfileResponse update(Long userId, ProfileUpdateRequest request);
    void delete(Long userId);
    ProfileResponse createForUser(Long userId, ProfileRequest request);
}
