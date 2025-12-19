package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.example.service.UserService;
import org.example.service.PostService;
import org.example.dto.request.PostRequest;
import org.example.dto.request.PostUpdateRequest;
import org.example.dto.response.PostResponse;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    // CREATE
    @PostMapping
    public ResponseEntity<EntityModel<UserResponse>> create(@Valid @RequestBody UserRequest request) {
        UserResponse created = userService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        EntityModel<UserResponse> model = EntityModel.of(created,
                linkTo(methodOn(UserController.class).getById(created.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).list()).withRel("users"));
        return ResponseEntity.created(location).body(model);
    }

    // LIST
    @GetMapping
    public CollectionModel<EntityModel<UserResponse>> list() {
        List<EntityModel<UserResponse>> items = userService.getAll().stream()
                .map(u -> EntityModel.of(u,
                        linkTo(methodOn(UserController.class).getById(u.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).list()).withRel("users")))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(UserController.class).list()).withSelfRel());
    }

    // GET by id
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> getById(@PathVariable Long id) {
        UserResponse resp = userService.getById(id);
        EntityModel<UserResponse> model = EntityModel.of(resp,
                linkTo(methodOn(UserController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).list()).withRel("users"),
                linkTo(methodOn(UserController.class).listUserPosts(id)).withRel("posts"));
        return ResponseEntity.ok(model);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> update(@PathVariable Long id, @Valid @RequestBody org.example.dto.request.UserUpdateRequest request) {
        UserResponse updated = userService.update(id, request);
        EntityModel<UserResponse> model = EntityModel.of(updated,
                linkTo(methodOn(UserController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).list()).withRel("users"));
        return ResponseEntity.ok(model);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Nested resource: posts for a user
    @GetMapping("/{userId}/posts")
    public CollectionModel<EntityModel<PostResponse>> listUserPosts(@PathVariable Long userId) {
        List<EntityModel<PostResponse>> items = postService.getByUserId(userId).stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(UserController.class).getUserPost(userId, p.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).listUserPosts(userId)).withRel("posts")))
                .toList();
        return CollectionModel.of(items, linkTo(methodOn(UserController.class).listUserPosts(userId)).withSelfRel());
    }

    @GetMapping("/{userId}/posts/{postId}")
    public ResponseEntity<EntityModel<PostResponse>> getUserPost(@PathVariable Long userId, @PathVariable Long postId) {
        PostResponse resp = postService.getByUserIdAndPostId(userId, postId);
        EntityModel<PostResponse> model = EntityModel.of(resp,
                linkTo(methodOn(UserController.class).getUserPost(userId, postId)).withSelfRel(),
                linkTo(methodOn(UserController.class).listUserPosts(userId)).withRel("posts"),
                linkTo(methodOn(UserController.class).getById(userId)).withRel("user"));
        return ResponseEntity.ok(model);
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<EntityModel<PostResponse>> createUserPost(@PathVariable Long userId, @RequestBody PostRequest request) {
        PostResponse created = postService.createForUser(userId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        EntityModel<PostResponse> model = EntityModel.of(created,
                linkTo(methodOn(UserController.class).getUserPost(userId, created.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).listUserPosts(userId)).withRel("posts"));
        return ResponseEntity.created(location).body(model);
    }

    @PutMapping("/{userId}/posts/{postId}")
    public ResponseEntity<EntityModel<PostResponse>> updateUserPost(@PathVariable Long userId, @PathVariable Long postId, @RequestBody PostUpdateRequest request) {
        PostResponse updated = postService.updateForUser(userId, postId, request);
        EntityModel<PostResponse> model = EntityModel.of(updated,
                linkTo(methodOn(UserController.class).getUserPost(userId, postId)).withSelfRel(),
                linkTo(methodOn(UserController.class).listUserPosts(userId)).withRel("posts"));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public ResponseEntity<Void> deleteUserPost(@PathVariable Long userId, @PathVariable Long postId) {
        postService.deleteForUser(userId, postId);
        return ResponseEntity.noContent().build();
    }
}