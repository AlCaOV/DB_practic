package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.StoriesRequest;
import org.example.dto.request.StoriesUpdateRequest;
import org.example.dto.response.StoriesResponse;
import org.example.entity.Stories;
import org.example.entity.User;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.StoriesMapper;
import org.example.repository.StoriesRepository;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.example.service.StoriesService;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoriesServiceImpl implements StoriesService {
    private final StoriesRepository repo;
    private final UserRepository userRepository;
    private final AuthService userHelper;

    @Override
    public StoriesResponse create(StoriesRequest request) {
        User currentUser = userHelper.getCurrentUser();
        Stories s = StoriesMapper.toEntity(request);
        s.setUser(currentUser);
        Stories saved = repo.save(s);
        return StoriesMapper.toResponse(saved);
    }

    @Override
    public List<StoriesResponse> getAll() {
        return repo.findAll().stream().map(StoriesMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public StoriesResponse getById(Long id) {
        Stories s = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Stories", id));
        return StoriesMapper.toResponse(s);
    }

    @Override
    public StoriesResponse update(Long id, StoriesUpdateRequest request) {
        Stories s = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Stories", id));

        User currentUser = userHelper.getCurrentUser();
        Long ownerId = (s.getUser() != null) ? s.getUser().getId() : -1L;

        if (!ownerId.equals(currentUser.getId())) {
            throw new AccessDeniedException("Ви не можете редагувати чужу сторі");
        }

        StoriesMapper.updateEntity(s, request);
        Stories saved = repo.save(s);
        return StoriesMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Stories s = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Stories", id));

        User currentUser = userHelper.getCurrentUser();
        Long ownerId = (s.getUser() != null) ? s.getUser().getId() : -1L;

        boolean isOwner = ownerId.equals(currentUser.getId());
        boolean isAdmin = userHelper.isAdmin();

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Ви не можете видалити чужу сторі");
        }

        repo.delete(s);
    }

    @Override
    public List<StoriesResponse> getByUserId(Long userId) {
        return repo.findByUserId(userId).stream().map(StoriesMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public StoriesResponse getByUserIdAndStoryId(Long userId, Long storyId) {
        Stories s = repo.findByIdAndUserId(storyId, userId).orElseThrow(() -> new EntityNotFoundException("Stories", storyId));
        return StoriesMapper.toResponse(s);
    }

    @Override
    public StoriesResponse createForUser(Long userId, StoriesRequest request) {
        User currentUser = userHelper.getCurrentUser();
        if (!currentUser.getId().equals(userId) && !userHelper.isAdmin()) {
            throw new AccessDeniedException("Не можна створювати сторі за іншого");
        }

        User u = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        Stories s = StoriesMapper.toEntity(request);
        s.setUser(u);
        Stories saved = repo.save(s);
        return StoriesMapper.toResponse(saved);
    }

    @Override
    public StoriesResponse updateForUser(Long userId, Long storyId, StoriesUpdateRequest request) {
        Stories s = repo.findByIdAndUserId(storyId, userId).orElseThrow(() -> new EntityNotFoundException("Stories", storyId));

        User currentUser = userHelper.getCurrentUser();
        if (!currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("Ви не можете редагувати цю сторі");
        }

        StoriesMapper.updateEntity(s, request);
        Stories saved = repo.save(s);
        return StoriesMapper.toResponse(saved);
    }

    @Override
    public void deleteForUser(Long userId, Long storyId) {
        Stories s = repo.findByIdAndUserId(storyId, userId).orElseThrow(() -> new EntityNotFoundException("Stories", storyId));

        User currentUser = userHelper.getCurrentUser();
        if (!currentUser.getId().equals(userId) && !userHelper.isAdmin()) {
            throw new AccessDeniedException("Ви не можете видалити цю сторі");
        }

        repo.delete(s);
    }
}