package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CommentRequest;
import org.example.dto.request.CommentUpdateRequest;
import org.example.dto.response.CommentResponse;
import org.example.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody CommentRequest req) {
        CommentResponse created = commentService.create(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public List<CommentResponse> list() { return commentService.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getById(@PathVariable Long id) { return ResponseEntity.ok(commentService.getById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long id, @RequestBody CommentUpdateRequest req) { return ResponseEntity.ok(commentService.update(id, req)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { commentService.delete(id); return ResponseEntity.noContent().build(); }

    // Nested under posts
    @GetMapping("/posts/{postId}")
    public CollectionModel<EntityModel<CommentResponse>> listPostComments(@PathVariable Long postId) {
        List<EntityModel<CommentResponse>> items = commentService.getByPostId(postId).stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CommentController.class).getPostComment(postId, c.getId())).withSelfRel(),
                        linkTo(methodOn(CommentController.class).listPostComments(postId)).withRel("comments")))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(CommentController.class).listPostComments(postId)).withSelfRel());
    }

    @GetMapping("/posts/{postId}/{commentId}")
    public ResponseEntity<EntityModel<CommentResponse>> getPostComment(@PathVariable Long postId, @PathVariable Long commentId) {
        CommentResponse resp = commentService.getByPostIdAndCommentId(postId, commentId);
        EntityModel<CommentResponse> model = EntityModel.of(resp,
                linkTo(methodOn(CommentController.class).getPostComment(postId, commentId)).withSelfRel(),
                linkTo(methodOn(CommentController.class).listPostComments(postId)).withRel("comments"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<EntityModel<CommentResponse>> createPostComment(@PathVariable Long postId, @RequestBody CommentRequest req) {
        CommentResponse created = commentService.createForPost(postId, req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        EntityModel<CommentResponse> model = EntityModel.of(created,
                linkTo(methodOn(CommentController.class).getPostComment(postId, created.getId())).withSelfRel(),
                linkTo(methodOn(CommentController.class).listPostComments(postId)).withRel("comments"));
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/posts/{postId}/{commentId}")
    public ResponseEntity<EntityModel<CommentResponse>> updatePostComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentUpdateRequest req) {
        CommentResponse updated = commentService.updateForPost(postId, commentId, req);
        EntityModel<CommentResponse> model = EntityModel.of(updated,
                linkTo(methodOn(CommentController.class).getPostComment(postId, commentId)).withSelfRel(),
                linkTo(methodOn(CommentController.class).listPostComments(postId)).withRel("comments"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/posts/{postId}/{commentId}")
    public ResponseEntity<Void> deletePostComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteForPost(postId, commentId);
        return ResponseEntity.noContent().build();
    }
}
