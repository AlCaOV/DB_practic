package org.example.mapper;

import org.example.dto.request.PostRequest;
import org.example.dto.request.PostUpdateRequest;
import org.example.dto.response.PostResponse;
import org.example.entity.Post;
import org.example.entity.User;

import java.time.LocalDateTime;

public class PostMapper {
    private PostMapper() {}

    public static Post toEntity(PostRequest req) {
        Post p = new Post();
        p.setCaption(req.caption());
        p.setLocation(req.location());
        p.setCreatedAt(LocalDateTime.now());
        if (req.userId() != null) {
            User u = new User(); u.setId(req.userId()); p.setUser(u);
        }
        p.setArchived(false);
        p.setStatus(Post.Status.active);
        return p;
    }

    public static void updateEntity(Post p, PostUpdateRequest req) {
        if (req.caption() != null) p.setCaption(req.caption());
        if (req.location() != null) p.setLocation(req.location());
        if (req.userId() != null) { User u = new User(); u.setId(req.userId()); p.setUser(u);}    }

    public static PostResponse toResponse(Post p) {
        return PostResponse.builder()
                .id(p.getId())
                .userId(p.getUser() != null ? p.getUser().getId() : null)
                .caption(p.getCaption())
                .createdAt(p.getCreatedAt())
                .location(p.getLocation())
                .isArchived(p.isArchived())
                .status(p.getStatus() != null ? p.getStatus().name() : null)
                .build();
    }
}

