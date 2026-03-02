package org.example.repository;

import org.example.entity.MediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
    List<MediaAsset> findByPostId(Long postId);
    Optional<MediaAsset> findByIdAndPostId(Long id, Long postId);
}
