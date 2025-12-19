package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.ProfileRequest;
import org.example.dto.request.ProfileUpdateRequest;
import org.example.dto.response.ProfileResponse;
import org.example.entity.Profile;
import org.example.entity.User;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.ProfileMapper;
import org.example.repository.ProfileRepository;
import org.example.repository.UserRepository;
import org.example.service.ProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository repo;
    private final UserRepository userRepository;

    @Override
    public ProfileResponse create(ProfileRequest request) {
        // Profile uses shared primary key mapped to User (MapsId). We must set a managed User entity.
        if (request.userId() == null) throw new IllegalArgumentException("userId is required for Profile creation");
        User user = userRepository.findById(request.userId()).orElseThrow(() -> new EntityNotFoundException("User", request.userId()));
        Profile p = ProfileMapper.toEntity(request);
        p.setUser(user); // set managed user so Hibernate can use its id for Profile PK
        Profile saved = repo.save(p);
        return ProfileMapper.toResponse(saved);
    }

    @Override
    public List<ProfileResponse> getAll() { return repo.findAll().stream().map(ProfileMapper::toResponse).collect(Collectors.toList()); }

    @Override
    public ProfileResponse getById(Long userId) {
        Profile p = repo.findById(userId).orElseThrow(() -> new EntityNotFoundException("Profile", userId));
        return ProfileMapper.toResponse(p);
    }

    @Override
    public ProfileResponse update(Long userId, ProfileUpdateRequest request) {
        Profile p = repo.findById(userId).orElseThrow(() -> new EntityNotFoundException("Profile", userId));
        ProfileMapper.updateEntity(p, request);
        // Do not change associated user via profile update — keep shared PK consistent
        Profile saved = repo.save(p);
        return ProfileMapper.toResponse(saved);
    }

    @Override
    public void delete(Long userId) { if (!repo.existsById(userId)) throw new EntityNotFoundException("Profile", userId); repo.deleteById(userId); }

    @Override
    public ProfileResponse createForUser(Long userId, ProfileRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        Profile p = ProfileMapper.toEntity(request);
        p.setUser(user);
        Profile saved = repo.save(p);
        return ProfileMapper.toResponse(saved);
    }
}
