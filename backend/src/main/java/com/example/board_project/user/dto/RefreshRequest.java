package com.example.board_project.user.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshRequest(
        @NotNull String username,
        @NotNull String refreshToken
) {}
