package com.example.board_project.user.repository;

import com.example.board_project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.board_project.global.response.ResponseCode.INTERNAL_SERVER_ERROR;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final long REFRESH_TOKEN_EXPIRATION = 7;

    public void save(String username, String refreshToken) {
        redisTemplate.opsForValue().set(username, refreshToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.DAYS);
    }

    public Optional<String> find(String username) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(username));
    }

    public void delete(String username) {
        log.info("Delete user: {}", username);
        try {
            Boolean result = redisTemplate.delete(username);
            if (Boolean.FALSE.equals(result)) {
                log.error("Delete user fail: {}", username);
                throw new CustomException(INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
    }
}