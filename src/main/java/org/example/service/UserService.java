package org.example.service;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest request);
    List<UserResponse> getAll();
    UserResponse getById(Long id);
    UserResponse update(Long id, org.example.dto.request.UserUpdateRequest request);
    void delete(Long id);

    // no nested post methods here; PostService handles nested post resources
}
