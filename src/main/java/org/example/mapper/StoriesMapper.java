package org.example.mapper;

import org.example.dto.request.StoriesRequest;
import org.example.dto.request.StoriesUpdateRequest;
import org.example.dto.response.StoriesResponse;
import org.example.entity.Post;
import org.example.entity.Stories;
import org.example.entity.User;

public class StoriesMapper {
    private StoriesMapper() {}

    public static Stories toEntity(StoriesRequest r) {
        Stories s = new Stories();
        s.setMediaUrl(r.mediaUrl());
        s.setCaption(r.caption());
        s.setDurationSec(r.durationSec());
        if (r.userId() != null) { User u = new User(); u.setId(r.userId()); s.setUser(u); }
        if (r.kind() != null) s.setKind(Stories.Kind.valueOf(r.kind().toLowerCase()));
        return s;
    }

    public static void updateEntity(Stories s, StoriesUpdateRequest r) {
        if (r.mediaUrl() != null) s.setMediaUrl(r.mediaUrl());
        if (r.caption() != null) s.setCaption(r.caption());
        if (r.durationSec() != null) s.setDurationSec(r.durationSec());
        if (r.userId() != null) { User u = new User(); u.setId(r.userId()); s.setUser(u); }
        if (r.kind() != null) s.setKind(Stories.Kind.valueOf(r.kind().toLowerCase()));
    }

    public static StoriesResponse toResponse(Stories s) {
        return StoriesResponse.builder().id(s.getId()).userId(s.getUser() != null ? s.getUser().getId() : null).mediaUrl(s.getMediaUrl()).kind(s.getKind() != null ? s.getKind().name() : null).caption(s.getCaption()).durationSec(s.getDurationSec()).build();
    }
}

