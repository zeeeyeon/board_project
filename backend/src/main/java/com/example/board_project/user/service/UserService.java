package com.example.board_project.user.service;

import com.example.board_project.global.exception.CustomException;
import com.example.board_project.global.response.ResponseCode;
import com.example.board_project.jwt.JwtUtil;
import com.example.board_project.user.dto.LoginRequest;
import com.example.board_project.user.dto.RefreshRequest;
import com.example.board_project.user.dto.SignupRequest;
import com.example.board_project.user.dto.TokenResponse;
import com.example.board_project.user.entity.Role;
import com.example.board_project.user.entity.User;
import com.example.board_project.user.repository.RefreshTokenRepository;
import com.example.board_project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        Role role = request.getRole();
        User user = new User(request.username(), passwordEncoder.encode(request.password()), role);
        userRepository.save(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ResponseCode.INVALID_PASSWORD);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole().name());

        String refreshToken = refreshTokenRepository.find(request.username())
                .orElseGet(() -> {
                    String newToken = jwtUtil.generateRefreshToken(request.username());
                    refreshTokenRepository.save(request.username(), newToken);
                    return newToken;
                });

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refreshToken(RefreshRequest request) {
        String storedRefreshToken = refreshTokenRepository.find(request.username())
                .orElseThrow(() -> new CustomException(ResponseCode.REFRESH_TOKEN_NOT_FOUND));

        if (!storedRefreshToken.equals(request.refreshToken()) || !jwtUtil.validateToken(request.refreshToken())) {
            throw new CustomException(ResponseCode.TOKEN_EXPIRED);
        }

        String newAccessToken = jwtUtil.generateAccessToken(request.username(), "USER");

        return new TokenResponse(newAccessToken, request.refreshToken());
    }

    @Transactional
    public void logout(String username) {
        refreshTokenRepository.delete(username);
    }
}