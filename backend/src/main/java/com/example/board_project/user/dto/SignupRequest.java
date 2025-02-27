package com.example.board_project.user.dto;

import com.example.board_project.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank @Size(min = 3, max = 20) String username,
        @NotBlank @Size(min = 8, max = 20) String password,
        Role role
) {
    public Role getRole() {
        return role != null ? role : Role.User;
    }
}
