package org.example.repository;

import org.example.entity.Stories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoriesRepository extends JpaRepository<Stories, Long> {
}
