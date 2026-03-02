package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.ProfileRequest;
import org.example.dto.request.ProfileUpdateRequest;
import org.example.dto.response.ProfileResponse;
import org.example.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileService service;

    @PostMapping
    public ResponseEntity<ProfileResponse> create(@RequestBody ProfileRequest req) { ProfileResponse created = service.create(req); URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getUserId()).toUri(); return ResponseEntity.created(location).body(created); }

    @GetMapping
    public List<ProfileResponse> list() { return service.getAll(); }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getById(@PathVariable Long userId) { return ResponseEntity.ok(service.getById(userId)); }

    @PutMapping("/{userId}")
    public ResponseEntity<ProfileResponse> update(@PathVariable Long userId, @RequestBody ProfileUpdateRequest req) { return ResponseEntity.ok(service.update(userId, req)); }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) { service.delete(userId); return ResponseEntity.noContent().build(); }

    // Nested under users
    @GetMapping("/users/{userId}")
    public EntityModel<ProfileResponse> getUserProfile(@PathVariable Long userId) {
        ProfileResponse resp = service.getById(userId);
        EntityModel<ProfileResponse> model = EntityModel.of(resp,
                linkTo(methodOn(ProfileController.class).getUserProfile(userId)).withSelfRel(),
                linkTo(methodOn(ProfileController.class).getById(userId)).withRel("profile"));
        return model;
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<EntityModel<ProfileResponse>> createUserProfile(@PathVariable Long userId, @RequestBody ProfileRequest req) {
        ProfileResponse created = service.createForUser(userId, req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        EntityModel<ProfileResponse> model = EntityModel.of(created,
                linkTo(methodOn(ProfileController.class).getUserProfile(userId)).withSelfRel());
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/users/{userId}")
    public EntityModel<ProfileResponse> updateUserProfile(@PathVariable Long userId, @RequestBody ProfileUpdateRequest req) {
        ProfileResponse updated = service.update(userId, req);
        return EntityModel.of(updated,
                linkTo(methodOn(ProfileController.class).getUserProfile(userId)).withSelfRel());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long userId) { service.delete(userId); return ResponseEntity.noContent().build(); }
}
