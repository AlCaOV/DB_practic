package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.MediaAssetRequest;
import org.example.dto.request.MediaAssetUpdateRequest;
import org.example.dto.response.MediaAssetResponse;
import org.example.entity.MediaAsset;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.MediaAssetMapper;
import org.example.repository.MediaAssetRepository;
import org.example.service.AuthService; // Імпорт
import org.example.service.MediaAssetService;
import org.springframework.security.access.AccessDeniedException; // Імпорт
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaAssetServiceImpl implements MediaAssetService {
    private final MediaAssetRepository repo;
    private final AuthService userHelper; // Додали

    @Override
    public MediaAssetResponse create(MediaAssetRequest request) {
        MediaAsset m = MediaAssetMapper.toEntity(request);
        MediaAsset saved = repo.save(m);
        return MediaAssetMapper.toResponse(saved);
    }

    @Override
    public List<MediaAssetResponse> getAll() { return repo.findAll().stream().map(MediaAssetMapper::toResponse).collect(Collectors.toList()); }

    @Override
    public MediaAssetResponse getById(Long id) { MediaAsset m = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("MediaAsset", id)); return MediaAssetMapper.toResponse(m); }

    @Override
    public MediaAssetResponse update(Long id, MediaAssetUpdateRequest request) { MediaAsset m = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("MediaAsset", id)); MediaAssetMapper.updateEntity(m, request); MediaAsset saved = repo.save(m); return MediaAssetMapper.toResponse(saved); }

    @Override
    public void delete(Long id) {
        MediaAsset m = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("MediaAsset", id));
        checkDeletePermissions(m);
        repo.deleteById(id);
    }

    @Override
    public List<MediaAssetResponse> getByPostId(Long postId) {
        return repo.findByPostId(postId).stream().map(MediaAssetMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public MediaAssetResponse getByPostIdAndMediaId(Long postId, Long mediaId) {
        MediaAsset m = repo.findByIdAndPostId(mediaId, postId).orElseThrow(() -> new EntityNotFoundException("MediaAsset", mediaId));
        return MediaAssetMapper.toResponse(m);
    }

    @Override
    public MediaAssetResponse createForPost(Long postId, MediaAssetRequest request) {
        // Тут можна додати перевірку, чи належить пост поточному юзеру
        MediaAsset m = MediaAssetMapper.toEntity(request);
        if (m.getPost() == null) {
            Post p = new Post();
            p.setId(postId);
            m.setPost(p);
        }
        MediaAsset saved = repo.save(m);
        return MediaAssetMapper.toResponse(saved);
    }

    @Override
    public MediaAssetResponse updateForPost(Long postId, Long mediaId, MediaAssetUpdateRequest request) {
        MediaAsset m = repo.findByIdAndPostId(mediaId, postId).orElseThrow(() -> new EntityNotFoundException("MediaAsset", mediaId));
        MediaAssetMapper.updateEntity(m, request);
        MediaAsset saved = repo.save(m);
        return MediaAssetMapper.toResponse(saved);
    }

    @Override
    public void deleteForPost(Long postId, Long mediaId) {
        MediaAsset m = repo.findByIdAndPostId(mediaId, postId).orElseThrow(() -> new EntityNotFoundException("MediaAsset", mediaId));
        checkDeletePermissions(m);
        repo.delete(m);
    }

    private void checkDeletePermissions(MediaAsset m) {
        User currentUser = userHelper.getCurrentUser();

        Long postOwnerId = -1L;
        if (m.getPost() != null && m.getPost().getUser() != null) {
            postOwnerId = m.getPost().getUser().getId();
        }

        boolean isPostOwner = postOwnerId.equals(currentUser.getId());
        boolean isAdmin = userHelper.isAdmin();

        if (!isPostOwner && !isAdmin) {
            throw new AccessDeniedException("Ви не можете видалити це медіа");
        }
    }
}