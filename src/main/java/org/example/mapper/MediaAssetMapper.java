package org.example.mapper;

import org.example.dto.request.MediaAssetRequest;
import org.example.dto.request.MediaAssetUpdateRequest;
import org.example.dto.response.MediaAssetResponse;
import org.example.entity.MediaAsset;
import org.example.entity.Post;

public class MediaAssetMapper {
    private MediaAssetMapper() {}

    public static MediaAsset toEntity(MediaAssetRequest r) {
        MediaAsset m = new MediaAsset();
        if (r.postId() != null) { Post p = new Post(); p.setId(r.postId()); m.setPost(p); }
        m.setKind(MediaAsset.Kind.valueOf(r.kind().toLowerCase()));
        m.setUrl(r.url());
        m.setDurationSec(r.durationSec());
        m.setPosition(r.position());
        return m;
    }

    public static void updateEntity(MediaAsset m, MediaAssetUpdateRequest r) {
        if (r.postId() != null) { Post p = new Post(); p.setId(r.postId()); m.setPost(p); }
        if (r.kind() != null) m.setKind(MediaAsset.Kind.valueOf(r.kind().toLowerCase()));
        if (r.url() != null) m.setUrl(r.url());
        if (r.durationSec() != null) m.setDurationSec(r.durationSec());
        if (r.position() != null) m.setPosition(r.position());
    }

    public static MediaAssetResponse toResponse(MediaAsset m) {
        return MediaAssetResponse.builder()
                .id(m.getId())
                .postId(m.getPost() != null ? m.getPost().getId() : null)
                .kind(m.getKind() != null ? m.getKind().name() : null)
                .url(m.getUrl())
                .durationSec(m.getDurationSec())
                .position(m.getPosition())
                .build();
    }
}

