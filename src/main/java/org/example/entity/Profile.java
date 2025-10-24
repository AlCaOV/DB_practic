package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "profile")
@Getter @Setter
public class Profile {
    @Id
    @Column(name = "user_id")
    private Long userId;

    private String fullName;
    private String bio;
    private String avatarUrl;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}