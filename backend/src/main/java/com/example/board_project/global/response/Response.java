package com.example.board_project.global.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class Response<T> {

    private final Status status;
    private final T content;

    @Getter
    @AllArgsConstructor
    private static class Status {
        private final int code;
        private final String message;
    }

    public static <T> Response<T> create(ResponseCode responseCode, T content) {
        return Response.<T>builder()
                .status(new Status(responseCode.getCode(), responseCode.getMessage()))
                .content(content)
                .build();
    }

    public static Response<Void> create(ResponseCode responseCode) {
        return Response.<Void>builder()
                .status(new Status(responseCode.getCode(), responseCode.getMessage()))
                .content(null)
                .build();
    }
}