package com.henrytran1803.BEBakeManage.user.service;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.user.dto.UserRequest;
import com.henrytran1803.BEBakeManage.user.entity.User;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    Optional<User> getUserByUserName(String userName);
    Optional<User> getUserById(int id);
    ApiResponse<User> createUser(User user, Set<String> roleNames);
    ApiResponse<User> updateUser(int id, UserRequest userRequest); // Cập nhật thông tin
    ApiResponse<Void> deactivateUser(int id); // Khóa tài khoản
}
