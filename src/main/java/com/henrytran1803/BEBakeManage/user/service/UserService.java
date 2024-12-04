package com.henrytran1803.BEBakeManage.user.service;
import com.beust.jcommander.IStringConverter;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.user.dto.CreateUserRequest;
import com.henrytran1803.BEBakeManage.user.dto.UserBasicDTO;
import com.henrytran1803.BEBakeManage.user.dto.UserRequest;
import com.henrytran1803.BEBakeManage.user.dto.UserResponseRegisterDTO;
import com.henrytran1803.BEBakeManage.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    Optional<User> getUserByUserName(String userName);
    Optional<User> getUserById(int id);

   /* ApiResponse<List<UserResponseRegisterDTO>> getActiveUsers(boolean isActive);*/

    ApiResponse<Page<UserResponseRegisterDTO>> getActiveUsers(String isActive, Pageable pageable);

    ApiResponse<UserResponseRegisterDTO> updateUser(int id, UserRequest userRequest); // Cập nhật thông tin
    ApiResponse<Void> deactivateUser(int id); // Khóa tài khoản
    ApiResponse<List<UserBasicDTO>> getAllUser();

    ApiResponse<UserResponseRegisterDTO> findUserById(int id);
}
