package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.PostRequest;
import org.example.dto.request.PostUpdateRequest;
import org.example.dto.response.PostResponse;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.PostMapper;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.example.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService userHelper;

    @Override
    public PostResponse create(PostRequest request) {
        User currentUser = userHelper.getCurrentUser();
        Post p = PostMapper.toEntity(request);
        p.setUser(currentUser);
        Post saved = postRepository.save(p);
        return PostMapper.toResponse(saved);
    }

    @Override
    public List<PostResponse> getAll() {
        return postRepository.findAll().stream().map(PostMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public PostResponse getById(Long id) {
        Post p = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post", id));
        return PostMapper.toResponse(p);
    }

    @Override
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post", id));

        User currentUser = userHelper.getCurrentUser();

        // Безпечна перевірка власника
        Long authorId = (post.getUser() != null) ? post.getUser().getId() : -1L;

        if (!authorId.equals(currentUser.getId())) {
            throw new AccessDeniedException("Ви не можете редагувати чужий пост");
        }

        PostMapper.updateEntity(post, request);
        Post saved = postRepository.save(post);
        return PostMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post", id));

        User currentUser = userHelper.getCurrentUser();

        // Безпечна перевірка власника (щоб не було NPE, якщо юзера видалили)
        Long postAuthorId = (post.getUser() != null) ? post.getUser().getId() : -1L;

        boolean isOwner = postAuthorId.equals(currentUser.getId());
        boolean isAdmin = userHelper.isAdmin();

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Ви не можете видалити цей пост");
        }

        postRepository.delete(post);
    }

    // nested resource implementations
    @Override
    public List<PostResponse> getByUserId(Long userId) {
        if (!userRepository.existsById(userId)) throw new EntityNotFoundException("User", userId);
        return postRepository.findByUserId(userId).stream().map(PostMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public PostResponse getByUserIdAndPostId(Long userId, Long postId) {
        Post p = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Post", postId));
        return PostMapper.toResponse(p);
    }

    @Override
    public PostResponse createForUser(Long userId, PostRequest request) {
        // Дозволяємо тільки для себе або адміна
        User currentUser = userHelper.getCurrentUser();
        if (!currentUser.getId().equals(userId) && !userHelper.isAdmin()) {
            throw new AccessDeniedException("Не можна створювати пости за інших");
        }

        User u = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User", userId));
        Post p = PostMapper.toEntity(request);
        p.setUser(u);
        Post saved = postRepository.save(p);
        return PostMapper.toResponse(saved);
    }

    @Override
    public PostResponse updateForUser(Long userId, Long postId, PostUpdateRequest request) {
        Post p = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Post", postId));

        User currentUser = userHelper.getCurrentUser();
        if (!currentUser.getId().equals(userId)) { // Тут адмін зазвичай не редагує контент, тільки видаляє
            throw new AccessDeniedException("Ви не можете редагувати цей пост");
        }

        PostMapper.updateEntity(p, request);
        Post saved = postRepository.save(p);
        return PostMapper.toResponse(saved);
    }

    @Override
    public void deleteForUser(Long userId, Long postId) {
        Post p = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Post", postId));

        User currentUser = userHelper.getCurrentUser();
        if (!currentUser.getId().equals(userId) && !userHelper.isAdmin()) {
            throw new AccessDeniedException("Ви не можете видалити цей пост");
        }

        postRepository.delete(p);
    }
}