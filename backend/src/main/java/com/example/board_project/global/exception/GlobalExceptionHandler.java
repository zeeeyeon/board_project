package com.example.board_project.global.exception;

import com.example.board_project.global.response.Response;
import com.example.board_project.global.response.ResponseCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public Response<Void> handleCustomException(CustomException e) {
        return Response.create(e.getResponseCode());
    }

    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception e) {
        return Response.create(ResponseCode.INTERNAL_SERVER_ERROR);
    }
}