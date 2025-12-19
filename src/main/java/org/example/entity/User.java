package org.example.entity;
import jakarta.persistence.*;
import lombok.*;
import org.example.entity.converter.UserStatusConverter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status", columnDefinition = "enum('active','inactive','banned')")
    @Convert(converter = UserStatusConverter.class)
    private Status status;

    public enum Status { active, inactive, banned }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
