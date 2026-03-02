package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.LikesRequest;
import org.example.dto.request.LikesUpdateRequest;
import org.example.dto.response.LikesResponse;
import org.example.service.LikesService;
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
@RequestMapping("/api/likes")
public class LikesController {
    private final LikesService service;

    @PostMapping
    public ResponseEntity<LikesResponse> create(@RequestBody LikesRequest req) { LikesResponse created = service.create(req); URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri(); return ResponseEntity.created(location).body(created); }

    @GetMapping
    public List<LikesResponse> list() { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<LikesResponse> getById(@PathVariable Long id) { return ResponseEntity.ok(service.getById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<LikesResponse> update(@PathVariable Long id, @RequestBody LikesUpdateRequest req) { return ResponseEntity.ok(service.update(id, req)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }

    // Nested under posts
    @GetMapping("/posts/{postId}")
    public CollectionModel<EntityModel<LikesResponse>> listPostLikes(@PathVariable Long postId) {
        List<EntityModel<LikesResponse>> items = service.getByPostId(postId).stream()
                .map(l -> EntityModel.of(l,
                        linkTo(methodOn(LikesController.class).getPostLike(postId, l.getId())).withSelfRel(),
                        linkTo(methodOn(LikesController.class).listPostLikes(postId)).withRel("likes")))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(LikesController.class).listPostLikes(postId)).withSelfRel());
    }

    @GetMapping("/posts/{postId}/{likeId}")
    public ResponseEntity<EntityModel<LikesResponse>> getPostLike(@PathVariable Long postId, @PathVariable Long likeId) {
        LikesResponse resp = service.getByPostIdAndLikeId(postId, likeId);
        EntityModel<LikesResponse> model = EntityModel.of(resp,
                linkTo(methodOn(LikesController.class).getPostLike(postId, likeId)).withSelfRel(),
                linkTo(methodOn(LikesController.class).listPostLikes(postId)).withRel("likes"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<EntityModel<LikesResponse>> createPostLike(@PathVariable Long postId, @RequestBody LikesRequest req) {
        LikesResponse created = service.createForPost(postId, req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        EntityModel<LikesResponse> model = EntityModel.of(created,
                linkTo(methodOn(LikesController.class).getPostLike(postId, created.getId())).withSelfRel(),
                linkTo(methodOn(LikesController.class).listPostLikes(postId)).withRel("likes"));
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/posts/{postId}/{likeId}")
    public ResponseEntity<EntityModel<LikesResponse>> updatePostLike(@PathVariable Long postId, @PathVariable Long likeId, @RequestBody LikesUpdateRequest req) {
        LikesResponse updated = service.updateForPost(postId, likeId, req);
        EntityModel<LikesResponse> model = EntityModel.of(updated,
                linkTo(methodOn(LikesController.class).getPostLike(postId, likeId)).withSelfRel(),
                linkTo(methodOn(LikesController.class).listPostLikes(postId)).withRel("likes"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/posts/{postId}/{likeId}")
    public ResponseEntity<Void> deletePostLike(@PathVariable Long postId, @PathVariable Long likeId) {
        service.deleteForPost(postId, likeId);
        return ResponseEntity.noContent().build();
    }
}
