package org.example.mapper;

import org.example.dto.request.CommentRequest;
import org.example.dto.request.CommentUpdateRequest;
import org.example.dto.response.CommentResponse;
import org.example.entity.Comment;
import org.example.entity.Post;
import org.example.entity.User;

import java.time.LocalDateTime;

public class CommentMapper {
    private CommentMapper() {}

    public static Comment toEntity(CommentRequest req) {
        Comment c = new Comment();
        c.setBody(req.body());
        c.setCreatedAt(LocalDateTime.now());
        if (req.postId() != null) { Post p = new Post(); p.setId(req.postId()); c.setPost(p); }
        if (req.userId() != null) { User u = new User(); u.setId(req.userId()); c.setUser(u); }
        return c;
    }

    public static void updateEntity(Comment c, CommentUpdateRequest req) {
        if (req.body() != null) c.setBody(req.body());
        if (req.postId() != null) { Post p = new Post(); p.setId(req.postId()); c.setPost(p); }
        if (req.userId() != null) { User u = new User(); u.setId(req.userId()); c.setUser(u); }
    }

    public static CommentResponse toResponse(Comment c) {
        return CommentResponse.builder()
                .id(c.getId())
                .postId(c.getPost() != null ? c.getPost().getId() : null)
                .userId(c.getUser() != null ? c.getUser().getId() : null)
                .body(c.getBody())
                .createdAt(c.getCreatedAt())
                .build();
    }
}

