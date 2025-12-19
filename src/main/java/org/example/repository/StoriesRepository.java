package org.example.repository;

import org.example.entity.Stories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoriesRepository extends JpaRepository<Stories, Long> {
    List<Stories> findByUserId(Long userId);
    Optional<Stories> findByIdAndUserId(Long id, Long userId);
}
