package org.example.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendshipResponse {
    private Long id;
    private Long followerId;
    private Long followeeId;
    private String status;
}

