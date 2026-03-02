package org.example.mapper;

import org.example.dto.request.ProfileRequest;
import org.example.dto.request.ProfileUpdateRequest;
import org.example.dto.response.ProfileResponse;
import org.example.entity.Profile;

public class ProfileMapper {
    private ProfileMapper() {}

    public static Profile toEntity(ProfileRequest r) {
        Profile p = new Profile();
        p.setFullName(r.fullName());
        p.setBio(r.bio());
        p.setAvatarUrl(r.avatarUrl());
        return p;
    }

    public static void updateEntity(Profile p, ProfileUpdateRequest r) {
        if (r.fullName() != null) p.setFullName(r.fullName());
        if (r.bio() != null) p.setBio(r.bio());
        if (r.avatarUrl() != null) p.setAvatarUrl(r.avatarUrl());
    }

    public static ProfileResponse toResponse(Profile p) {
        return ProfileResponse.builder().userId(p.getUser() != null ? p.getUser().getId() : p.getUserId()).fullName(p.getFullName()).bio(p.getBio()).avatarUrl(p.getAvatarUrl()).build();
    }
}
