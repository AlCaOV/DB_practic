package org.example.repository;

import org.example.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByPostId(Long postId);
    Optional<Likes> findByIdAndPostId(Long id, Long postId);

    List<Likes> findByUserId(Long userId);
    Optional<Likes> findByIdAndUserId(Long id, Long userId);
}
