package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "stories")
@Getter @Setter
public class Stories {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "media_url")
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    private Kind kind; // image, video

    private String caption;

    @Column(name = "created_at", columnDefinition = "date default CURRENT_DATE")
    private LocalDate createdAt;

    @Column(name = "duration_sec")
    private Integer durationSec;

    public enum Kind { image, video }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDate.now();
    }
}
