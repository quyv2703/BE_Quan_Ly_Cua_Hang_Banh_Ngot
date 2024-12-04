package com.henrytran1803.BEBakeManage.user.service;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.common.util.JwtUtils;
import com.henrytran1803.BEBakeManage.user.dto.LoginResponse;
import com.henrytran1803.BEBakeManage.user.dto.UserDTO;
import com.henrytran1803.BEBakeManage.user.dto.UserResponseRegisterDTO;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.entity.Role;
import com.henrytran1803.BEBakeManage.user.repository.RoleRepository;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponse login(String email, String password) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password)
                );

                Set<String> roles = user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet());

                String token = jwtUtils.generateJwtToken(Math.toIntExact(user.getId()), String.join(",", roles));
                UserDTO userDTO = new UserDTO(Math.toIntExact(user.getId()), user.getEmail(), roles);

                return new LoginResponse(userDTO, token);
            } else {
                throw new RuntimeException("Không tìm thấy tài khoản người dùng");
            }
        } catch (Exception e) {
            throw new RuntimeException("Thông tin đăng nhập không hợp lệ", e);
        }
    }

    @Transactional
    public ApiResponse<UserResponseRegisterDTO> register(String firstName, String lastName, String email,
                                                         Date dateOfBirth, String password, List<Integer> roleIds) {
        synchronized (this) {
            if (userRepository.findByEmail(email).isPresent()) {
                return ApiResponse.Q_failure(null, QuyExeption.EMAIL_ALREADY_EXISTS);
            }
        }
        Set<Role> roles = new HashSet<>();
        for (Integer roleId : roleIds) {
            Optional<Role> roleOptional = roleRepository.findById(Math.toIntExact(roleId));
            if (roleOptional.isEmpty()) {
                return ApiResponse.Q_failure(null, QuyExeption.ROLE_NOT_FOUND);
            }
            roles.add(roleOptional.get());
        }
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setDateOfBirth(dateOfBirth);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        user.setIsActive(true);
        User saveUser =userRepository.save(user);
        UserResponseRegisterDTO userResponse = new UserResponseRegisterDTO(
                saveUser.getId(),
                saveUser.getFirstName(),
                saveUser.getLastName(),
                saveUser.getEmail(),
                saveUser.getDateOfBirth().toString(),
                saveUser.getIsActive(),
                saveUser.getRoles().stream()
                        .map(Role::getId)
                        .collect(Collectors.toSet()));
        return ApiResponse.Q_success(userResponse, QuyExeption.SUCCESS);
    }
}