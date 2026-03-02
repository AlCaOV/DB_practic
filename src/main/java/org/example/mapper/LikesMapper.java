package org.example.mapper;

import org.example.dto.request.LikesRequest;
import org.example.dto.request.LikesUpdateRequest;
import org.example.dto.response.LikesResponse;
import org.example.entity.Likes;
import org.example.entity.Post;
import org.example.entity.User;

public class LikesMapper {
    private LikesMapper() {}

    public static Likes toEntity(LikesRequest r) {
        Likes l = new Likes();
        if (r.postId() != null) { Post p = new Post(); p.setId(r.postId()); l.setPost(p); }
        if (r.userId() != null) { User u = new User(); u.setId(r.userId()); l.setUser(u); }
        return l;
    }

    public static void updateEntity(Likes l, LikesUpdateRequest r) {
        if (r.postId() != null) { Post p = new Post(); p.setId(r.postId()); l.setPost(p); }
        if (r.userId() != null) { User u = new User(); u.setId(r.userId()); l.setUser(u); }
    }

    public static LikesResponse toResponse(Likes l) {
        return LikesResponse.builder().id(l.getId()).postId(l.getPost() != null ? l.getPost().getId() : null).userId(l.getUser() != null ? l.getUser().getId() : null).build();
    }
}

