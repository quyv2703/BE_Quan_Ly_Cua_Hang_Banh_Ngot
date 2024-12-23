package com.henrytran1803.BEBakeManage.user.controller;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.user.dto.*;
import com.henrytran1803.BEBakeManage.user.entity.Role;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/user")
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

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Page<UserResponseRegisterDTO>>> getActiveUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<Page<UserResponseRegisterDTO>> response = userService.getActiveUsers(String.valueOf(true), pageable);

        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping("/inactive")
    public ResponseEntity<ApiResponse<Page<UserResponseRegisterDTO>>> getInactiveUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        ApiResponse<Page<UserResponseRegisterDTO>> response = userService.getActiveUsers(String.valueOf(false), pageable);

        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(response);
    }

    // API kích hoạt tài khoản người dùng
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable int id) {
        ApiResponse<Void> response = userService.activateUser(id);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 nếu thành công
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // HTTP 400 nếu lỗi
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


    @GetMapping("")
    public ResponseEntity<ApiResponse<List<UserBasicDTO>>> getActiveUsers() {
        ApiResponse<List<UserBasicDTO>> response = userService.getAllUser();
        return ResponseEntity.ok(response);
    }



}
