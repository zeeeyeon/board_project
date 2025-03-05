package com.example.board_project.admin.controller;

import com.example.board_project.global.response.Response;
import com.example.board_project.global.response.ResponseCode;
import com.example.board_project.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/check")
    public Response<?> checkAdmin(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails instanceof User user && user.getRole().name().equals("ADMIN")) {
            return Response.create(ResponseCode.SUCCESS, "관리자 계정입니다.");
        }
        return Response.create(ResponseCode.FORBIDDEN, "관리자 계정이 아닙니다.");
    }
}
