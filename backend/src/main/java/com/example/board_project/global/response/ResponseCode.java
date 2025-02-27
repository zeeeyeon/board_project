package com.example.board_project.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(200, "성공"),
    CREATED(201, "생성 완료"),
    LOGOUT_SUCCESS(200, "로그아웃 성공"),

    BAD_REQUEST(400, "잘못된 요청"),
    UNAUTHORIZED(401, "인증이 필요합니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    NOT_FOUND(404, "리소스를 찾을 수 없습니다."),

    INTERNAL_SERVER_ERROR(500, "서버 내부 오류"),

    INVALID_PASSWORD(401, "잘못된 비밀번호입니다."),
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    TOKEN_EXPIRED(401, "토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(401, "Refresh Token이 존재하지 않습니다.");

    private final int code;
    private final String message;
}
