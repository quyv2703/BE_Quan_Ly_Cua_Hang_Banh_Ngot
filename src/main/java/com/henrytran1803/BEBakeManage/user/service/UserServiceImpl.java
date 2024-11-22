package com.henrytran1803.BEBakeManage.user.service;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.user.dto.UserRequest;
import com.henrytran1803.BEBakeManage.user.entity.Role;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.repository.RoleRepository;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByEmail(userName);
    }
    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public ApiResponse<User> createUser(User user, Set<String> roleNames) {
        // Kiểm tra xem Email đã tồn tại chưa
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ApiResponse.Q_failure(null, QuyExeption.EMAIL_ALREADY_EXISTS);
        }

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Optional<Role> roleOptional = roleRepository.findByName(roleName);
            if (roleOptional.isEmpty()) {
                return ApiResponse.Q_failure(null, QuyExeption.ROLE_NOT_FOUND); // Role không tồn tại
            }
            roles.add(roleOptional.get());
        }
        user.setRoles(roles);

        // Lưu user vào cơ sở dữ liệu
        User savedUser = userRepository.save(user);
        return ApiResponse.Q_success(savedUser, QuyExeption.SUCCESS);
    }

    @Override
    public ApiResponse<User> updateUser(int id, UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_NOT_FOUND);
        }

        User user = optionalUser.get();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword()); // Nên mã hóa mật khẩu ở đây
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setActive(userRequest.getIsActive());
        userRepository.save(user);

        return ApiResponse.Q_success(user, QuyExeption.SUCCESS);
    }

    @Override
    public ApiResponse<Void> deactivateUser(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_NOT_FOUND);
        }

        User user = optionalUser.get();
        user.setActive(false); // Khóa tài khoản
        userRepository.save(user);

        return ApiResponse.Q_success(null, QuyExeption.SUCCESS);
    }

}
