package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.StoriesRequest;
import org.example.dto.request.StoriesUpdateRequest;
import org.example.dto.response.StoriesResponse;
import org.example.service.StoriesService;
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
@RequestMapping("/api/stories")
public class StoriesController {
    private final StoriesService service;

    @PostMapping
    public ResponseEntity<StoriesResponse> create(@RequestBody StoriesRequest req) { StoriesResponse created = service.create(req); URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri(); return ResponseEntity.created(location).body(created); }

    @GetMapping
    public List<StoriesResponse> list() { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<StoriesResponse> getById(@PathVariable Long id) { return ResponseEntity.ok(service.getById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<StoriesResponse> update(@PathVariable Long id, @RequestBody StoriesUpdateRequest req) { return ResponseEntity.ok(service.update(id, req)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }

    // Nested under users
    @GetMapping("/users/{userId}")
    public CollectionModel<EntityModel<StoriesResponse>> listUserStories(@PathVariable Long userId) {
        List<EntityModel<StoriesResponse>> items = service.getAll().stream() // no service method getByUserId defined, fallback to filtering by userId via existing getAll responses
                .filter(s -> s.getUserId() != null && s.getUserId().equals(userId))
                .map(s -> EntityModel.of(s,
                        linkTo(methodOn(StoriesController.class).getUserStory(userId, s.getId())).withSelfRel(),
                        linkTo(methodOn(StoriesController.class).listUserStories(userId)).withRel("stories")))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(StoriesController.class).listUserStories(userId)).withSelfRel());
    }

    @GetMapping("/users/{userId}/{storyId}")
    public ResponseEntity<EntityModel<StoriesResponse>> getUserStory(@PathVariable Long userId, @PathVariable Long storyId) {
        // attempt to get by id and ensure it belongs to user
        StoriesResponse resp = service.getById(storyId);
        if (resp == null || resp.getUserId() == null || !resp.getUserId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<StoriesResponse> model = EntityModel.of(resp,
                linkTo(methodOn(StoriesController.class).getUserStory(userId, storyId)).withSelfRel(),
                linkTo(methodOn(StoriesController.class).listUserStories(userId)).withRel("stories"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<EntityModel<StoriesResponse>> createUserStory(@PathVariable Long userId, @RequestBody StoriesRequest req) {
        // assume service.create can accept userId in the request DTO; if not, create normally and relying on request.userId
        StoriesResponse created = service.create(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        EntityModel<StoriesResponse> model = EntityModel.of(created,
                linkTo(methodOn(StoriesController.class).getUserStory(userId, created.getId())).withSelfRel(),
                linkTo(methodOn(StoriesController.class).listUserStories(userId)).withRel("stories"));
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/users/{userId}/{storyId}")
    public ResponseEntity<EntityModel<StoriesResponse>> updateUserStory(@PathVariable Long userId, @PathVariable Long storyId, @RequestBody StoriesUpdateRequest req) {
        StoriesResponse updated = service.update(storyId, req);
        EntityModel<StoriesResponse> model = EntityModel.of(updated,
                linkTo(methodOn(StoriesController.class).getUserStory(userId, storyId)).withSelfRel(),
                linkTo(methodOn(StoriesController.class).listUserStories(userId)).withRel("stories"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/users/{userId}/{storyId}")
    public ResponseEntity<Void> deleteUserStory(@PathVariable Long userId, @PathVariable Long storyId) {
        StoriesResponse resp = service.getById(storyId);
        if (resp == null || resp.getUserId() == null || !resp.getUserId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }
        service.delete(storyId);
        return ResponseEntity.noContent().build();
    }
}
