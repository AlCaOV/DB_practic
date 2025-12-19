package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.MediaAssetRequest;
import org.example.dto.request.MediaAssetUpdateRequest;
import org.example.dto.response.MediaAssetResponse;
import org.example.entity.MediaAsset;
import org.example.entity.Post;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.MediaAssetMapper;
import org.example.repository.MediaAssetRepository;
import org.example.service.MediaAssetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaAssetServiceImpl implements MediaAssetService {
    private final MediaAssetRepository repo;

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
    public void delete(Long id) { if (!repo.existsById(id)) throw new EntityNotFoundException("MediaAsset", id); repo.deleteById(id); }

    // nested resource methods (by post)
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
        repo.delete(m);
    }
}
