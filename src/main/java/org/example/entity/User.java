package org.example.entity;
import jakarta.persistence.*;
import lombok.*;
import org.example.entity.converter.UserStatusConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    // 1. Якщо видаляємо Юзера -> видаляються всі його ПОСТИ
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Важливо, щоб не було циклічних посилань при логуванні
    private List<Post> posts = new ArrayList<>();

    // 2. Якщо видаляємо Юзера -> видаляються всі його СТОРІЗ
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Stories> stories = new ArrayList<>();

    // 3. Якщо видаляємо Юзера -> видаляється Профіль
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    // (Опціонально) Видаляти коментарі юзера при видаленні юзера
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    // (Опціонально) Видаляти лайки юзера
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Likes> likes = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profiles;

    // 4. Підписки (кого я читаю): Якщо видаляємо Юзера -> видаляємо його підписки
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Friendship> following = new ArrayList<>();

    // 5. Підписники (хто мене читає): Якщо видаляємо Юзера -> видаляємо записи про підписників
    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Friendship> followers = new ArrayList<>();


}
