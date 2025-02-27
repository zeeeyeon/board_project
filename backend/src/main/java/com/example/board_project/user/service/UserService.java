package com.example.board_project.user.service;

import com.example.board_project.global.exception.CustomException;
import com.example.board_project.global.response.ResponseCode;
import com.example.board_project.jwt.JwtUtil;
import com.example.board_project.user.dto.LoginRequest;
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
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        refreshTokenRepository.save(user.getUsername(), refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refreshToken(String username, String refreshToken) {
        String savedToken = refreshTokenRepository.find(username)
                .orElseThrow(() -> new CustomException(ResponseCode.REFRESH_TOKEN_NOT_FOUND));

        if (!savedToken.equals(refreshToken) || !jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(ResponseCode.TOKEN_EXPIRED);
        }

        String newAccessToken = jwtUtil.generateAccessToken(username, "USER");
        return new TokenResponse(newAccessToken, refreshToken);
    }

    @Transactional
    public void logout(String username) {
        refreshTokenRepository.delete(username);
    }
}
