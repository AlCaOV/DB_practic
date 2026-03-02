package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CommentRequest;
import org.example.dto.request.CommentUpdateRequest;
import org.example.dto.response.CommentResponse;
import org.example.entity.Comment;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.CommentMapper;
import org.example.repository.CommentRepository;
import org.example.repository.PostRepository;
import org.example.service.AuthService;
import org.example.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService userHelper;

    @Override
    public CommentResponse create(CommentRequest request) {
        User currentUser = userHelper.getCurrentUser();
        Comment c = CommentMapper.toEntity(request);
        c.setUser(currentUser);
        Comment saved = commentRepository.save(c);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public List<CommentResponse> getAll() {
        return commentRepository.findAll().stream().map(CommentMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public CommentResponse getById(Long id) {
        Comment c = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment", id));
        return CommentMapper.toResponse(c);
    }

    @Override
    public CommentResponse update(Long id, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment", id));

        User currentUser = userHelper.getCurrentUser();

        // Safe check for comment author
        Long authorId = (comment.getUser() != null) ? comment.getUser().getId() : -1L;

        if (!authorId.equals(currentUser.getId())) {
            throw new AccessDeniedException("Ви не можете редагувати чужий коментар");
        }

        CommentMapper.updateEntity(comment, request);
        Comment saved = commentRepository.save(comment);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment", id));

        checkDeletePermissions(comment);
        commentRepository.delete(comment);
    }

    @Override
    public CommentResponse createForPost(Long postId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post", postId));

        User currentUser = userHelper.getCurrentUser();

        Comment comment = CommentMapper.toEntity(request);
        comment.setPost(post);
        comment.setUser(currentUser);

        Comment saved = commentRepository.save(comment);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public List<CommentResponse> getByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream().map(CommentMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public CommentResponse getByPostIdAndCommentId(Long postId, Long commentId) {
        Comment c = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        return CommentMapper.toResponse(c);
    }

    @Override
    public CommentResponse updateForPost(Long postId, Long commentId, CommentUpdateRequest request) {
        Comment c = commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

        User currentUser = userHelper.getCurrentUser();
        Long authorId = (c.getUser() != null) ? c.getUser().getId() : -1L;

        if (!authorId.equals(currentUser.getId())) {
            throw new AccessDeniedException("Ви не можете редагувати чужий коментар");
        }

        CommentMapper.updateEntity(c, request);
        Comment saved = commentRepository.save(c);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public void deleteForPost(Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

        checkDeletePermissions(comment);
        commentRepository.delete(comment);
    }

    // Приватний метод для перевірки прав видалення (DRY principle)
    private void checkDeletePermissions(Comment comment) {
        User currentUser = userHelper.getCurrentUser();
        Long currentUserId = currentUser.getId();

        Long commentAuthorId = (comment.getUser() != null) ? comment.getUser().getId() : -1L;

        Long postAuthorId = -1L;
        if (comment.getPost() != null && comment.getPost().getUser() != null) {
            postAuthorId = comment.getPost().getUser().getId();
        }

        boolean isCommentOwner = currentUserId.equals(commentAuthorId);
        boolean isPostOwner = currentUserId.equals(postAuthorId);
        boolean isAdmin = userHelper.isAdmin();

        if (!isCommentOwner && !isPostOwner && !isAdmin) {
            throw new AccessDeniedException("Ви не маєте права видаляти цей коментар");
        }
    }

    @Override
    public CommentResponse createForUser(Long userId, CommentRequest request) {
        User u = new User(); u.setId(userId);
        Comment reqEntity = CommentMapper.toEntity(request);
        reqEntity.setUser(u);
        Comment saved = commentRepository.save(reqEntity);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public List<CommentResponse> getByUserId(Long userId) {
        return commentRepository.findByUserId(userId).stream().map(CommentMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public CommentResponse getByUserIdAndCommentId(Long userId, Long commentId) {
        Comment c = commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        return CommentMapper.toResponse(c);
    }

    @Override
    public CommentResponse updateForUser(Long userId, Long commentId, CommentUpdateRequest request) {
        Comment c = commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));

        // Тут також можна додати перевірку, якщо потрібно

        CommentMapper.updateEntity(c, request);
        Comment saved = commentRepository.save(c);
        return CommentMapper.toResponse(saved);
    }

    @Override
    public void deleteForUser(Long userId, Long commentId) {
        Comment c = commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(() -> new EntityNotFoundException("Comment", commentId));
        checkDeletePermissions(c);
        commentRepository.delete(c);
    }
}