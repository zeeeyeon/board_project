package com.example.board_project.user.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
