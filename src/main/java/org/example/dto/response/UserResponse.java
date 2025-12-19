package org.example.dto.response;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String passwordHash;
    private LocalDateTime createdAt;
    private String status;
    private String email;
    private String username;
}
