package com.henrytran1803.BEBakeManage.user.controller;

import com.henrytran1803.BEBakeManage.common.exception.error.ErrorCode;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.nofication.model.NotificationMessage;
import com.henrytran1803.BEBakeManage.user.dto.*;
import com.henrytran1803.BEBakeManage.user.service.AuthService;
import com.henrytran1803.BEBakeManage.user.service.UserService;
import com.henrytran1803.BEBakeManage.websocket.WebSocketSessionManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Email đăng nhập: " + loginRequest.getEmail());
        try {
            LoginResponse token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ApiResponse.success(token);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Không tìm thấy người dùng hoặc thông tin đăng nhập không chính xác",
                    ErrorCode.USER_NOT_FOUND.getCode(), null);
        }
    }

    /**
     * API Đăng ký tài khoản người dùng mới với các quyền được chỉ định

     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseRegisterDTO>> register( @Valid @RequestBody RegisterRequest registerRequest) {
        ApiResponse<UserResponseRegisterDTO> response = authService.register(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                registerRequest.getDateOfBirth(),
                registerRequest.getPassword(),
                registerRequest.getRoleIds()
        );

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}