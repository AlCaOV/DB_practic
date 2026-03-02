package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.PostRequest;
import org.example.dto.request.PostUpdateRequest;
import org.example.dto.response.PostResponse;
import org.example.service.PostService;
import org.example.service.CommentService;
import org.example.dto.request.CommentRequest;
import org.example.dto.request.CommentUpdateRequest;
import org.example.dto.response.CommentResponse;
import org.example.service.LikesService;
import org.example.dto.request.LikesRequest;
import org.example.dto.request.LikesUpdateRequest;
import org.example.dto.response.LikesResponse;
import org.example.service.MediaAssetService;
import org.example.dto.request.MediaAssetRequest;
import org.example.dto.request.MediaAssetUpdateRequest;
import org.example.dto.response.MediaAssetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final LikesService likesService;
    private final MediaAssetService mediaAssetService;

    @PostMapping
    public ResponseEntity<EntityModel<PostResponse>> create(@RequestBody PostRequest req) {
        PostResponse created = postService.create(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        EntityModel<PostResponse> model = EntityModel.of(created,
                linkTo(methodOn(PostController.class).getById(created.getId())).withSelfRel(),
                linkTo(methodOn(PostController.class).list()).withRel("posts"));
        return ResponseEntity.created(location).body(model);
    }

    @GetMapping
    public CollectionModel<EntityModel<PostResponse>> list() {
        List<EntityModel<PostResponse>> items = postService.getAll().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PostController.class).getById(p.getId())).withSelfRel(),
                        linkTo(methodOn(PostController.class).list()).withRel("posts")))
                .collect(Collectors.toList());
        return CollectionModel.of(items, linkTo(methodOn(PostController.class).list()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PostResponse>> getById(@PathVariable Long id) {
        PostResponse resp = postService.getById(id);
        EntityModel<PostResponse> model = EntityModel.of(resp,
                linkTo(methodOn(PostController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(PostController.class).list()).withRel("posts"));
        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PostResponse>> update(@PathVariable Long id, @RequestBody PostUpdateRequest req) {
        PostResponse updated = postService.update(id, req);
        EntityModel<PostResponse> model = EntityModel.of(updated,
                linkTo(methodOn(PostController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(PostController.class).list()).withRel("posts"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { postService.delete(id); return ResponseEntity.noContent().build(); }

    // --- Nested resource: comments for a post (/api/posts/{postId}/comments)
    @GetMapping("/{postId}/comments")
    public CollectionModel<EntityModel<CommentResponse>> listPostComments(@PathVariable Long postId) {
        List<EntityModel<CommentResponse>> items = commentService.getByPostId(postId).stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(PostController.class).getPostComment(postId, c.getId())).withSelfRel(),
                        linkTo(methodOn(PostController.class).listPostComments(postId)).withRel("comments")))
                .collect(Collectors.toList());
        return CollectionModel.of(items, linkTo(methodOn(PostController.class).listPostComments(postId)).withSelfRel());
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<EntityModel<CommentResponse>> getPostComment(@PathVariable Long postId, @PathVariable Long commentId) {
        CommentResponse resp = commentService.getByPostIdAndCommentId(postId, commentId);
        EntityModel<CommentResponse> model = EntityModel.of(resp,
                linkTo(methodOn(PostController.class).getPostComment(postId, commentId)).withSelfRel(),
                linkTo(methodOn(PostController.class).listPostComments(postId)).withRel("comments"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<EntityModel<CommentResponse>> createPostComment(@PathVariable Long postId, @RequestBody CommentRequest req) {
        CommentResponse created = commentService.createForPost(postId, req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        EntityModel<CommentResponse> model = EntityModel.of(created,
                linkTo(methodOn(PostController.class).getPostComment(postId, created.getId())).withSelfRel(),
                linkTo(methodOn(PostController.class).listPostComments(postId)).withRel("comments"));
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<EntityModel<CommentResponse>> updatePostComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentUpdateRequest req) {
        CommentResponse updated = commentService.updateForPost(postId, commentId, req);
        EntityModel<CommentResponse> model = EntityModel.of(updated,
                linkTo(methodOn(PostController.class).getPostComment(postId, commentId)).withSelfRel(),
                linkTo(methodOn(PostController.class).listPostComments(postId)).withRel("comments"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deletePostComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteForPost(postId, commentId);
        return ResponseEntity.noContent().build();
    }

    // --- Nested resource: likes for a post (/api/posts/{postId}/likes)
    @GetMapping("/{postId}/likes")
    public CollectionModel<EntityModel<LikesResponse>> listPostLikes(@PathVariable Long postId) {
        List<EntityModel<LikesResponse>> items = likesService.getByPostId(postId).stream()
                .map(l -> EntityModel.of(l,
                        linkTo(methodOn(PostController.class).getPostLike(postId, l.getId())).withSelfRel(),
                        linkTo(methodOn(PostController.class).listPostLikes(postId)).withRel("likes")))
                .collect(Collectors.toList());
        return CollectionModel.of(items, linkTo(methodOn(PostController.class).listPostLikes(postId)).withSelfRel());
    }

    @GetMapping("/{postId}/likes/{likeId}")
    public ResponseEntity<EntityModel<LikesResponse>> getPostLike(@PathVariable Long postId, @PathVariable Long likeId) {
        LikesResponse resp = likesService.getByPostIdAndLikeId(postId, likeId);
        EntityModel<LikesResponse> model = EntityModel.of(resp,
                linkTo(methodOn(PostController.class).getPostLike(postId, likeId)).withSelfRel(),
                linkTo(methodOn(PostController.class).listPostLikes(postId)).withRel("likes"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<EntityModel<LikesResponse>> createPostLike(@PathVariable Long postId, @RequestBody LikesRequest req) {
        LikesResponse created = likesService.createForPost(postId, req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        EntityModel<LikesResponse> model = EntityModel.of(created,
                linkTo(methodOn(PostController.class).getPostLike(postId, created.getId())).withSelfRel(),
                linkTo(methodOn(PostController.class).listPostLikes(postId)).withRel("likes"));
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{postId}/likes/{likeId}")
    public ResponseEntity<EntityModel<LikesResponse>> updatePostLike(@PathVariable Long postId, @PathVariable Long likeId, @RequestBody LikesUpdateRequest req) {
        LikesResponse updated = likesService.updateForPost(postId, likeId, req);
        EntityModel<LikesResponse> model = EntityModel.of(updated,
                linkTo(methodOn(PostController.class).getPostLike(postId, likeId)).withSelfRel(),
                linkTo(methodOn(PostController.class).listPostLikes(postId)).withRel("likes"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{postId}/likes/{likeId}")
    public ResponseEntity<Void> deletePostLike(@PathVariable Long postId, @PathVariable Long likeId) {
        likesService.deleteForPost(postId, likeId);
        return ResponseEntity.noContent().build();
    }

    // --- Nested resource: media-assets for a post (/api/posts/{postId}/media-assets)
    @GetMapping("/{postId}/media-assets")
    public CollectionModel<EntityModel<MediaAssetResponse>> listPostMedia(@PathVariable Long postId) {
        List<EntityModel<MediaAssetResponse>> items = mediaAssetService.getByPostId(postId).stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(PostController.class).getPostMedia(postId, m.getId())).withSelfRel(),
                        linkTo(methodOn(PostController.class).listPostMedia(postId)).withRel("media-assets")))
                .collect(Collectors.toList());
        return CollectionModel.of(items, linkTo(methodOn(PostController.class).listPostMedia(postId)).withSelfRel());
    }

    @GetMapping("/{postId}/media-assets/{mediaId}")
    public ResponseEntity<EntityModel<MediaAssetResponse>> getPostMedia(@PathVariable Long postId, @PathVariable Long mediaId) {
        MediaAssetResponse resp = mediaAssetService.getByPostIdAndMediaId(postId, mediaId);
        EntityModel<MediaAssetResponse> model = EntityModel.of(resp,
                linkTo(methodOn(PostController.class).getPostMedia(postId, mediaId)).withSelfRel(),
                linkTo(methodOn(PostController.class).listPostMedia(postId)).withRel("media-assets"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/{postId}/media-assets")
    public ResponseEntity<EntityModel<MediaAssetResponse>> createPostMedia(@PathVariable Long postId, @RequestBody MediaAssetRequest req) {
        MediaAssetResponse created = mediaAssetService.createForPost(postId, req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        EntityModel<MediaAssetResponse> model = EntityModel.of(created,
                linkTo(methodOn(PostController.class).getPostMedia(postId, created.getId())).withSelfRel(),
                linkTo(methodOn(PostController.class).listPostMedia(postId)).withRel("media-assets"));
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{postId}/media-assets/{mediaId}")
    public ResponseEntity<EntityModel<MediaAssetResponse>> updatePostMedia(@PathVariable Long postId, @PathVariable Long mediaId, @RequestBody MediaAssetUpdateRequest req) {
        MediaAssetResponse updated = mediaAssetService.updateForPost(postId, mediaId, req);
        EntityModel<MediaAssetResponse> model = EntityModel.of(updated,
                linkTo(methodOn(PostController.class).getPostMedia(postId, mediaId)).withSelfRel(),
                linkTo(methodOn(PostController.class).listPostMedia(postId)).withRel("media-assets"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{postId}/media-assets/{mediaId}")
    public ResponseEntity<Void> deletePostMedia(@PathVariable Long postId, @PathVariable Long mediaId) {
        mediaAssetService.deleteForPost(postId, mediaId);
        return ResponseEntity.noContent().build();
    }

}
