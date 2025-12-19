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
    @ManyToOne @JoinColumn(name = "follower_id")
    private User follower;
    @ManyToOne @JoinColumn(name = "followee_id")
    private User followee;
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