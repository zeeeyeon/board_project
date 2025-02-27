package com.example.board_project.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final long REFRESH_TOKEN_EXPIRATION = 7; // 7일

    public void save(String username, String refreshToken) {
        redisTemplate.opsForValue().set(username, refreshToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.DAYS);
    }

    public Optional<String> find(String username) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(username));
    }

    public void delete(String username) {
        redisTemplate.delete(username);
    }
}