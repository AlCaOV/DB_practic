package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mediaasset")
@Getter @Setter
public class MediaAsset {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private Kind kind; // image or video

    private String url;
    private Integer durationSec;
    private Integer position;

    public enum Kind { image, video }
}
