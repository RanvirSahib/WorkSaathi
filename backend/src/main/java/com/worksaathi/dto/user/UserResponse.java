package com.worksaathi.dto.user;

import com.worksaathi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String profileImage;
    private Boolean isActive;
    private Boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getRole() != null ? user.getRole().name() : null,
            user.getProfileImage(),
            user.getIsActive(),
            user.getIsVerified(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
