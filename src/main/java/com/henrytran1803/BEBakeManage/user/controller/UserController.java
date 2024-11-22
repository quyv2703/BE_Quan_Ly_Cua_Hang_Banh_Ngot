package com.henrytran1803.BEBakeManage.user.controller;

import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.user.dto.CreateUserRequest;
import com.henrytran1803.BEBakeManage.user.dto.UserDTO;
import com.henrytran1803.BEBakeManage.user.dto.UserRequest;
import com.henrytran1803.BEBakeManage.user.entity.Role;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class UserController {
    @Autowired
    private UserService userService;

    // API sửa thông tin người dùng
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable int id,
            @Validated @RequestBody UserRequest userRequest) {
        ApiResponse<User> response = userService.updateUser(id, userRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 nếu thành công
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // HTTP 404 nếu không tìm thấy
        }
    }

    // API khóa tài khoản người dùng
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable int id) {
        ApiResponse<Void> response = userService.deactivateUser(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // HTTP 200 nếu thành công
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // HTTP 404 nếu không tìm thấy
        }
    }
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        user.setDateOfBirth(createUserRequest.getDateOfBirth());
        user.setActive(createUserRequest.getIsActive());

        ApiResponse<User> response = userService.createUser(user, createUserRequest.getRoles());

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // HTTP 201
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // HTTP 400
        }
    }

    @GetMapping("/info")
    public UserDTO getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        int id = Integer.valueOf((String)authentication.getPrincipal());

        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found")); // Ném ngoại lệ nếu không tìm thấy
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getRoles().stream()                     // Bắt đầu stream từ các vai trò
                        .map(Role::getName)                     // Chuyển đổi từng Role thành tên của nó
                        .collect(Collectors.toSet())             // Thu thập lại thành Set<String>
        );    }





}
