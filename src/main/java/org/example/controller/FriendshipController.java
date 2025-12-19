package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.FriendshipRequest;
import org.example.dto.request.FriendshipUpdateRequest;
import org.example.dto.response.FriendshipResponse;
import org.example.service.FriendshipService;
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
@RequestMapping("/api/friendships")
public class FriendshipController {
    private final FriendshipService service;

    @PostMapping
    public ResponseEntity<FriendshipResponse> create(@RequestBody FriendshipRequest req) { FriendshipResponse created = service.create(req); URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri(); return ResponseEntity.created(location).body(created); }

    @GetMapping
    public List<FriendshipResponse> list() { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<FriendshipResponse> getById(@PathVariable Long id) { return ResponseEntity.ok(service.getById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<FriendshipResponse> update(@PathVariable Long id, @RequestBody FriendshipUpdateRequest req) { return ResponseEntity.ok(service.update(id, req)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }

    // Nested under users - friend relations for a user (both follower and followee perspectives)
    @GetMapping("/users/{userId}/followers")
    public CollectionModel<EntityModel<FriendshipResponse>> listUserFollowers(@PathVariable Long userId) {
        List<EntityModel<FriendshipResponse>> items = service.getAll().stream()
                .filter(f -> f.getFolloweeId() != null && f.getFolloweeId().equals(userId))
                .map(f -> EntityModel.of(f,
                        linkTo(methodOn(FriendshipController.class).getUserFollower(userId, f.getId())).withSelfRel(),
                        linkTo(methodOn(FriendshipController.class).listUserFollowers(userId)).withRel("followers")))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(FriendshipController.class).listUserFollowers(userId)).withSelfRel());
    }

    @GetMapping("/users/{userId}/followers/{friendshipId}")
    public ResponseEntity<EntityModel<FriendshipResponse>> getUserFollower(@PathVariable Long userId, @PathVariable Long friendshipId) {
        FriendshipResponse resp = service.getById(friendshipId);
        if (resp == null || resp.getFolloweeId() == null || !resp.getFolloweeId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<FriendshipResponse> model = EntityModel.of(resp,
                linkTo(methodOn(FriendshipController.class).getUserFollower(userId, friendshipId)).withSelfRel(),
                linkTo(methodOn(FriendshipController.class).listUserFollowers(userId)).withRel("followers"));
        return ResponseEntity.ok(model);
    }

    @GetMapping("/users/{userId}/followees")
    public CollectionModel<EntityModel<FriendshipResponse>> listUserFollowees(@PathVariable Long userId) {
        List<EntityModel<FriendshipResponse>> items = service.getAll().stream()
                .filter(f -> f.getFollowerId() != null && f.getFollowerId().equals(userId))
                .map(f -> EntityModel.of(f,
                        linkTo(methodOn(FriendshipController.class).getUserFollowee(userId, f.getId())).withSelfRel(),
                        linkTo(methodOn(FriendshipController.class).listUserFollowees(userId)).withRel("followees")))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(FriendshipController.class).listUserFollowees(userId)).withSelfRel());
    }

    @GetMapping("/users/{userId}/followees/{friendshipId}")
    public ResponseEntity<EntityModel<FriendshipResponse>> getUserFollowee(@PathVariable Long userId, @PathVariable Long friendshipId) {
        FriendshipResponse resp = service.getById(friendshipId);
        if (resp == null || resp.getFollowerId() == null || !resp.getFollowerId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<FriendshipResponse> model = EntityModel.of(resp,
                linkTo(methodOn(FriendshipController.class).getUserFollowee(userId, friendshipId)).withSelfRel(),
                linkTo(methodOn(FriendshipController.class).listUserFollowees(userId)).withRel("followees"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/users/{userId}/followees")
    public ResponseEntity<EntityModel<FriendshipResponse>> createUserFollowee(@PathVariable Long userId, @RequestBody FriendshipRequest req) {
        FriendshipResponse created = service.create(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        EntityModel<FriendshipResponse> model = EntityModel.of(created,
                linkTo(methodOn(FriendshipController.class).getUserFollowee(userId, created.getId())).withSelfRel(),
                linkTo(methodOn(FriendshipController.class).listUserFollowees(userId)).withRel("followees"));
        return ResponseEntity.created(location).body(model);
    }

    @DeleteMapping("/users/{userId}/{friendshipId}")
    public ResponseEntity<Void> deleteUserFriendship(@PathVariable Long userId, @PathVariable Long friendshipId) {
        service.delete(friendshipId);
        return ResponseEntity.noContent().build();
    }
}
