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
import org.example.service.AuthService; // Імпорт
import org.example.service.ProfileService;
import org.springframework.security.access.AccessDeniedException; // Імпорт
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository repo;
    private final UserRepository userRepository;
    private final AuthService userHelper; // Додали

    @Override
    public ProfileResponse create(ProfileRequest request) {
        if (request.userId() == null) throw new IllegalArgumentException("userId is required for Profile creation");

        // Перевірка: чи створюємо ми профіль для себе?
        User currentUser = userHelper.getCurrentUser();
        if (!currentUser.getId().equals(request.userId()) && !userHelper.isAdmin()) {
            throw new AccessDeniedException("Ви не можете створити профіль для іншого користувача");
        }

        User user = userRepository.findById(request.userId()).orElseThrow(() -> new EntityNotFoundException("User", request.userId()));
        Profile p = ProfileMapper.toEntity(request);
        p.setUser(user);
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

        User currentUser = userHelper.getCurrentUser();
        if (!currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("Ви не можете редагувати чужий профіль");
        }

        ProfileMapper.updateEntity(p, request);
        Profile saved = repo.save(p);
        return ProfileMapper.toResponse(saved);
    }

    @Override
    public void delete(Long userId) {
        if (!repo.existsById(userId)) throw new EntityNotFoundException("Profile", userId);

        User currentUser = userHelper.getCurrentUser();
        boolean isOwner = currentUser.getId().equals(userId);
        boolean isAdmin = userHelper.isAdmin();

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Ви не можете видалити чужий профіль");
        }

        repo.deleteById(userId);
    }

    @Override
    public ProfileResponse createForUser(Long userId, ProfileRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        Profile p = ProfileMapper.toEntity(request);
        p.setUser(user);
        Profile saved = repo.save(p);
        return ProfileMapper.toResponse(saved);
    }
}