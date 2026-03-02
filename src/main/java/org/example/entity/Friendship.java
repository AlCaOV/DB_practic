package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower; // Це має співпадати з mappedBy = "follower"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id") // або user_id, залежить як ти назвав
    private User followee; // Це має співпадати з mappedBy = "followee"
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        accepted, rejected, pending
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}