package org.example.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponse {
    private Long userId;
    private String fullName;
    private String bio;
    private String avatarUrl;
}

