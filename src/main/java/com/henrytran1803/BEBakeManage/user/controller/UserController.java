package com.henrytran1803.BEBakeManage.user.controller;

import com.henrytran1803.BEBakeManage.user.dto.UserDTO;
import com.henrytran1803.BEBakeManage.user.entity.Role;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class UserController {
    @Autowired
    private UserService userService;
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
