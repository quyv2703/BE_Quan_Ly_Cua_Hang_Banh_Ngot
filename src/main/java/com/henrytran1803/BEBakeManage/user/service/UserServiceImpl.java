package com.henrytran1803.BEBakeManage.user.service;

import com.henrytran1803.BEBakeManage.common.exception.error.QuyExeption;
import com.henrytran1803.BEBakeManage.common.response.ApiResponse;
import com.henrytran1803.BEBakeManage.user.dto.UserBasicDTO;
import com.henrytran1803.BEBakeManage.user.dto.UserRequest;
import com.henrytran1803.BEBakeManage.user.dto.UserResponseRegisterDTO;
import com.henrytran1803.BEBakeManage.user.entity.Role;
import com.henrytran1803.BEBakeManage.user.entity.User;
import com.henrytran1803.BEBakeManage.user.repository.RoleRepository;
import com.henrytran1803.BEBakeManage.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByEmail(userName);
    }
    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }



    @Override
    public ApiResponse<Page<UserResponseRegisterDTO>> getActiveUsers(String isActive, Pageable pageable) {
        try {
            Page<User> users;
            if (isActive.equals("all")) {
                users = userRepository.findAll(pageable);
            } else if (isActive.equals("active")) {
                users = userRepository.findByIsActive(true, pageable);
            } else if (isActive.equals("inactive")) {
                users = userRepository.findByIsActive(false, pageable);
            } else {
                users = userRepository.findAll(pageable);
            }
            Page<UserResponseRegisterDTO> userDTOs = users.map(user -> new UserResponseRegisterDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getDateOfBirth().toString(),
                    user.getIsActive(),
                    user.getRoles().stream()
                            .map(Role::getId)
                            .collect(Collectors.toSet())
            ));

            return ApiResponse.Q_success(userDTOs, QuyExeption.SUCCESS);
        } catch (Exception e) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_NOT_FOUND);
        }
    }
    @Override
    @Transactional
    public ApiResponse<UserResponseRegisterDTO> updateUser(int id, UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setDateOfBirth(userRequest.getDateOfBirth());
        Set<Role> roles = new HashSet<>();
        for (Integer roleId : userRequest.getRoleIds()) {
            Optional<Role> roleOptional = roleRepository.findById(Math.toIntExact(roleId));
            if (roleOptional.isEmpty()) {
                return ApiResponse.Q_failure(null, QuyExeption.ROLE_NOT_FOUND);
            }
            roles.add(roleOptional.get());
        }
        user.setRoles(roles);
        userRepository.save(user);
        UserResponseRegisterDTO userDTO = new UserResponseRegisterDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDateOfBirth().toString(),
                user.getIsActive(),
                user.getRoles().stream()
                        .map(Role::getId)
                        .collect(Collectors.toSet())
        );
        return ApiResponse.Q_success(userDTO, QuyExeption.SUCCESS);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deactivateUser(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        if(user.getIsActive() == true){
            user.setIsActive(false);
        }else{
            user.setIsActive(true);
        }
        userRepository.save(user);

        return ApiResponse.Q_success(null, QuyExeption.SUCCESS); // Thành công
    }
    @Transactional
    @Override
    public ApiResponse<List<UserBasicDTO>> getAllUser() {
        List<UserBasicDTO> users = userRepository.findAllBasicInfo();
        return ApiResponse.success(users);
    }

    @Override
    public ApiResponse<UserResponseRegisterDTO> findUserById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return ApiResponse.Q_failure(null, QuyExeption.USER_NOT_FOUND);
        }

        User user = optionalUser.get();
        UserResponseRegisterDTO userDTO = new UserResponseRegisterDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDateOfBirth().toString(),
                user.getIsActive(),
                // Chuyển đổi Set<Role> thành Set<Long> (id của roles)
                user.getRoles().stream()
                        .map(Role::getId) // Giả sử Role có phương thức getId() để lấy ID
                        .collect(Collectors.toSet())
        );

        return ApiResponse.Q_success(userDTO, QuyExeption.SUCCESS);
    }

}
