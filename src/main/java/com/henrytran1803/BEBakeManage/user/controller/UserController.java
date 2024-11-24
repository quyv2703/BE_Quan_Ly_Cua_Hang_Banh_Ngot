package com.henrytran1803.BEBakeManage.user.controller;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.user.dto.CreateUserRequest;
import com.henrytran1803.BEBakeManage.user.dto.UserDTO;
import com.henrytran1803.BEBakeManage.user.dto.UserRequest;
import com.henrytran1803.BEBakeManage.user.dto.UserResponseRegisterDTO;
import com.henrytran1803.BEBakeManage.user.entity.Role;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class UserController {
    @Autowired
    private UserService userService;

    // API sửa thông tin người dùng
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseRegisterDTO>> updateUser(
            @PathVariable int id,
            @Valid @RequestBody UserRequest userRequest) {
        ApiResponse<UserResponseRegisterDTO> response = userService.updateUser(id, userRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 nếu thành công
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // HTTP 404 nếu không tìm thấy
        }
    }

    // API khóa tài khoản người dùng
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable int id) {
        ApiResponse<Void> response = userService.deactivateUser(id);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 nếu thành công
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // HTTP 400 nếu lỗi
        }
    }

    // Lấy danh sách user active
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<UserResponseRegisterDTO>>> getActiveUsers() {
        ApiResponse<List<UserResponseRegisterDTO>> response = userService.getActiveUsers(true);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response); // HTTP 204 nếu không có user
        }
    }

    // Lấy danh sách user non-active
    @GetMapping("/inactive")
    public ResponseEntity<ApiResponse<List<UserResponseRegisterDTO>>> getInactiveUsers() {
        ApiResponse<List<UserResponseRegisterDTO>> response = userService.getActiveUsers(false);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response); // HTTP 204 nếu không có user
        }
    }

    // Lấy thông tin user theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseRegisterDTO>> getUserById(@PathVariable int id) {
        ApiResponse<UserResponseRegisterDTO> response = userService.findUserById(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 nếu tìm thấy
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // HTTP 404 nếu không tìm thấy
        }
    }






}
