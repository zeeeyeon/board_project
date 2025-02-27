package com.example.board_project.user.controller;

import com.example.board_project.global.response.Response;
import com.example.board_project.global.response.ResponseCode;
import com.example.board_project.user.dto.LoginRequest;
import com.example.board_project.user.dto.SignupRequest;
import com.example.board_project.user.dto.TokenResponse;
import com.example.board_project.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Response<Void> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request);
        return Response.create(ResponseCode.CREATED);
    }

    @PostMapping("/login")
    public Response<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return Response.create(ResponseCode.SUCCESS, userService.login(request));
    }

    @PostMapping("/refresh")
    public Response<TokenResponse> refreshToken(@RequestParam String username, @RequestParam String refreshToken) {
        return Response.create(ResponseCode.SUCCESS, userService.refreshToken(username, refreshToken));
    }

    @PostMapping("/logout")
    public Response<Void> logout(@RequestParam String username) {
        userService.logout(username);
        return Response.create(ResponseCode.LOGOUT_SUCCESS);
    }
}
