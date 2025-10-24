package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "post")
@Getter @Setter
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "text")
    private String caption;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String location;

    @Column(name = "is_archived")
    private boolean isArchived;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status { active, inactive, banned }
}