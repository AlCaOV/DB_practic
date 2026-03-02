package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.MediaAssetRequest;
import org.example.dto.request.MediaAssetUpdateRequest;
import org.example.dto.response.MediaAssetResponse;
import org.example.service.MediaAssetService;
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
@RequestMapping("/api/media-assets")
public class MediaAssetController {
    private final MediaAssetService service;

    @PostMapping
    public ResponseEntity<MediaAssetResponse> create(@RequestBody MediaAssetRequest req) { MediaAssetResponse created = service.create(req); URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri(); return ResponseEntity.created(location).body(created); }

    @GetMapping
    public List<MediaAssetResponse> list() { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<MediaAssetResponse> getById(@PathVariable Long id) { return ResponseEntity.ok(service.getById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<MediaAssetResponse> update(@PathVariable Long id, @RequestBody MediaAssetUpdateRequest req) { return ResponseEntity.ok(service.update(id, req)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }

    // Nested under posts
    @GetMapping("/posts/{postId}")
    public CollectionModel<EntityModel<MediaAssetResponse>> listPostMedia(@PathVariable Long postId) {
        List<EntityModel<MediaAssetResponse>> items = service.getByPostId(postId).stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(MediaAssetController.class).getPostMedia(postId, m.getId())).withSelfRel(),
                        linkTo(methodOn(MediaAssetController.class).listPostMedia(postId)).withRel("media-assets")))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(MediaAssetController.class).listPostMedia(postId)).withSelfRel());
    }

    @GetMapping("/posts/{postId}/{mediaId}")
    public ResponseEntity<EntityModel<MediaAssetResponse>> getPostMedia(@PathVariable Long postId, @PathVariable Long mediaId) {
        MediaAssetResponse resp = service.getByPostIdAndMediaId(postId, mediaId);
        EntityModel<MediaAssetResponse> model = EntityModel.of(resp,
                linkTo(methodOn(MediaAssetController.class).getPostMedia(postId, mediaId)).withSelfRel(),
                linkTo(methodOn(MediaAssetController.class).listPostMedia(postId)).withRel("media-assets"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<EntityModel<MediaAssetResponse>> createPostMedia(@PathVariable Long postId, @RequestBody MediaAssetRequest req) {
        MediaAssetResponse created = service.createForPost(postId, req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        EntityModel<MediaAssetResponse> model = EntityModel.of(created,
                linkTo(methodOn(MediaAssetController.class).getPostMedia(postId, created.getId())).withSelfRel(),
                linkTo(methodOn(MediaAssetController.class).listPostMedia(postId)).withRel("media-assets"));
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/posts/{postId}/{mediaId}")
    public ResponseEntity<EntityModel<MediaAssetResponse>> updatePostMedia(@PathVariable Long postId, @PathVariable Long mediaId, @RequestBody MediaAssetUpdateRequest req) {
        MediaAssetResponse updated = service.updateForPost(postId, mediaId, req);
        EntityModel<MediaAssetResponse> model = EntityModel.of(updated,
                linkTo(methodOn(MediaAssetController.class).getPostMedia(postId, mediaId)).withSelfRel(),
                linkTo(methodOn(MediaAssetController.class).listPostMedia(postId)).withRel("media-assets"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/posts/{postId}/{mediaId}")
    public ResponseEntity<Void> deletePostMedia(@PathVariable Long postId, @PathVariable Long mediaId) {
        service.deleteForPost(postId, mediaId);
        return ResponseEntity.noContent().build();
    }
}
